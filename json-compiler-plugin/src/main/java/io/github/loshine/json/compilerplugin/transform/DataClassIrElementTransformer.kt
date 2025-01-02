package io.github.loshine.json.compilerplugin.transform

import io.github.loshine.json.compilerplugin.DebugLogger
import org.jetbrains.kotlin.backend.common.IrElementTransformerVoidWithContext
import org.jetbrains.kotlin.backend.common.extensions.IrPluginContext
import org.jetbrains.kotlin.ir.IrStatement
import org.jetbrains.kotlin.ir.declarations.IrClass
import org.jetbrains.kotlin.ir.declarations.IrProperty
import org.jetbrains.kotlin.ir.expressions.IrConst
import org.jetbrains.kotlin.ir.expressions.impl.IrConstImpl
import org.jetbrains.kotlin.ir.expressions.impl.IrConstructorCallImpl
import org.jetbrains.kotlin.ir.util.constructors
import org.jetbrains.kotlin.ir.util.getAnnotation
import org.jetbrains.kotlin.ir.util.hasAnnotation
import org.jetbrains.kotlin.ir.util.packageFqName
import org.jetbrains.kotlin.name.ClassId
import org.jetbrains.kotlin.name.FqName

internal class DataClassIrElementTransformer(
    private val pluginContext: IrPluginContext,
    private val packages: List<String>,
    private val debugLogger: DebugLogger
) : IrElementTransformerVoidWithContext() {

    companion object {
        private val classSerializableAnnotationFqName =
            FqName("kotlinx.serialization.Serializable")
        private val propertySerialNameAnnotationFqName = FqName("kotlinx.serialization.SerialName")
        private val propertyTransientAnnotationFqName = FqName("kotlinx.serialization.Transient")
        private val fastJsonPropertyAnnotationClassId =
            ClassId.fromString("com/alibaba/fastjson/annotation/JSONField")
        private val fastJson2PropertyAnnotationClassId =
            ClassId.fromString("com/alibaba/fastjson2/annotation/JSONField")
    }

    override fun visitClassNew(declaration: IrClass): IrStatement {
        // 没有要处理的包的话，就不处理了
        if (packages.none { declaration.packageFqName?.asString()?.contains(it) == true }) {
            return super.visitClassNew(declaration)
        }
        if (declaration.isData && declaration.hasAnnotation(classSerializableAnnotationFqName)) {
            debugLogger.log("Data class ${declaration.name.asString()} annotate with kotlinx.serialization.Serializable")
            declaration.declarations.filterIsInstance<IrProperty>().forEach { property ->
                handleSerialName(declaration, property)
                handleTransient(declaration, property)
            }
        }
        return super.visitClassNew(declaration)
    }

    private fun handleSerialName(declaration: IrClass, property: IrProperty) {
        if (property.hasAnnotation(propertySerialNameAnnotationFqName)) {
            debugLogger.log(
                "Data class ${declaration.name.asString()} " +
                        "Field: ${property.name} has SerialName"
            )
            val serialNameAnnotation = property.getAnnotation(propertySerialNameAnnotationFqName)

            val jsonFieldAnnotation = pluginContext.referenceClass(
                fastJsonPropertyAnnotationClassId
            )?.constructors?.firstOrNull()?.owner ?: return
            val serialNameValue = serialNameAnnotation?.getValueArgument(0)
            val serialNameStringValue: String =
                (if (serialNameValue is IrConst<*>) serialNameValue.value.toString() else null)
                    ?: return
            property.backingField?.annotations =
                property.backingField?.annotations.orEmpty() + listOf(
                    IrConstructorCallImpl.fromSymbolOwner(
                        startOffset = property.startOffset,
                        endOffset = property.endOffset,
                        type = jsonFieldAnnotation.returnType,
                        constructorSymbol = jsonFieldAnnotation.symbol
                    ).apply {
                        // name 位于第二个
                        putValueArgument(
                            1, IrConstImpl.string(
                                startOffset = property.startOffset,
                                endOffset = property.endOffset,
                                type = pluginContext.irBuiltIns.stringType,
                                value = serialNameStringValue
                            )
                        )
                    }
                )
        }
    }

    private fun handleTransient(declaration: IrClass, property: IrProperty) {
        if (property.hasAnnotation(propertyTransientAnnotationFqName)) {
            debugLogger.log(
                "Data class ${declaration.name.asString()}" +
                        " Field: ${property.name} has Transient"
            )

            val jsonFieldAnnotation = pluginContext.referenceClass(
                fastJsonPropertyAnnotationClassId
            )?.constructors?.firstOrNull()?.owner
            // fastjson
            if (jsonFieldAnnotation != null) {
                property.backingField?.annotations =
                    property.backingField?.annotations.orEmpty() + listOf(
                        IrConstructorCallImpl.fromSymbolOwner(
                            startOffset = property.startOffset,
                            endOffset = property.endOffset,
                            type = jsonFieldAnnotation.returnType,
                            constructorSymbol = jsonFieldAnnotation.symbol
                        ).apply {
                            // serialize 位于第 3, boolean
                            // deserialize 4, boolean
                            putValueArgument(
                                3, IrConstImpl.boolean(
                                    startOffset = property.startOffset,
                                    endOffset = property.endOffset,
                                    type = pluginContext.irBuiltIns.stringType,
                                    value = false
                                )
                            )
                            putValueArgument(
                                4, IrConstImpl.boolean(
                                    startOffset = property.startOffset,
                                    endOffset = property.endOffset,
                                    type = pluginContext.irBuiltIns.stringType,
                                    value = false
                                )
                            )
                        }
                    )
            }

            val jsonFieldAnnotationFastJson2 = pluginContext.referenceClass(
                fastJson2PropertyAnnotationClassId
            )?.constructors?.firstOrNull()?.owner
            if (jsonFieldAnnotationFastJson2 != null) {
                property.backingField?.annotations =
                    property.backingField?.annotations.orEmpty() + listOf(
                        IrConstructorCallImpl.fromSymbolOwner(
                            startOffset = property.startOffset,
                            endOffset = property.endOffset,
                            type = jsonFieldAnnotationFastJson2.returnType,
                            constructorSymbol = jsonFieldAnnotationFastJson2.symbol
                        ).apply {
                            // serialize 位于第 4, boolean
                            // deserialize 5, boolean
                            putValueArgument(
                                4, IrConstImpl.boolean(
                                    startOffset = property.startOffset,
                                    endOffset = property.endOffset,
                                    type = pluginContext.irBuiltIns.stringType,
                                    value = false
                                )
                            )
                            putValueArgument(
                                5, IrConstImpl.boolean(
                                    startOffset = property.startOffset,
                                    endOffset = property.endOffset,
                                    type = pluginContext.irBuiltIns.stringType,
                                    value = false
                                )
                            )
                        }
                    )
            }
        }
    }
}
