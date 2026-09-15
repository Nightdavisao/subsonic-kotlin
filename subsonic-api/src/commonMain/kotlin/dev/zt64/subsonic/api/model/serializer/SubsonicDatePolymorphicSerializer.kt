package dev.zt64.subsonic.api.model.serializer

import kotlinx.datetime.LocalDate
import kotlinx.datetime.serializers.FormattedLocalDateSerializer
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

internal object SubsonicDatePolymorphicSerializer: JsonContentPolymorphicSerializer<LocalDate>(LocalDate::class) {
    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<LocalDate> {
        try {
            element.jsonObject
            return SubsonicDateSerializer
        } catch (_: IllegalArgumentException) {
            return NavidromeDateSerializer
        }
    }

    private object SubsonicDateSerializer : FormattedLocalDateSerializer(
        "dev.zt64.subsonic.api.model.serializer.SubsonicDateSerializer", LocalDate.Format {
            year()
            chars("-")
            monthNumber()
            chars("-")
            day()
        }
    )

    private object NavidromeDateSerializer : KSerializer<LocalDate> {
        override val descriptor = PrimitiveSerialDescriptor(
            "dev.zt64.subsonic.api.model.serializer.NavidromeDateSerializer",
            PrimitiveKind.STRING
        )

        override fun serialize(
            encoder: Encoder,
            value: LocalDate
        ) {
            TODO("Not yet implemented")
        }

        override fun deserialize(decoder: Decoder): LocalDate {
            val value = decoder.decodeSerializableValue(JsonElement.serializer()).jsonObject
            return LocalDate(
                value["year"]?.jsonPrimitive?.int ?: 2000,
                value["month"]?.jsonPrimitive?.int ?: 1,
                value["day"]?.jsonPrimitive?.int ?: 1,
            )
        }
    }
}