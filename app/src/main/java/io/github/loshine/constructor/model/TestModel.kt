package io.github.loshine.constructor.model

import com.alibaba.fastjson.annotation.JSONField
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class TestModel(
    @JSONField(name = "jsonTestFoo") @SerialName("testFoo") val foo: String? = null,
    @JSONField(serialize = false, deserialize = false) @SerialName("testBar") var bar: Int? = null
) {
    @Transient
    val transientField: Float? = null
}
