package com.wutsi.checkout.access.dto

import org.springframework.format.`annotation`.DateTimeFormat
import java.time.OffsetDateTime
import kotlin.Double
import kotlin.Long
import kotlin.String

public data class OrderSummary(
    public val id: String = "",
    public val shortId: String = "",
    public val storeId: Long = 0,
    public val status: String = "",
    public val subTotalPrice: Long = 0,
    public val totalDiscount: Double = 0.0,
    public val totalPrice: Double = 0.0,
    public val currency: String = "",
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val created: OffsetDateTime = OffsetDateTime.now(),
    @get:DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssZ")
    public val updated: OffsetDateTime = OffsetDateTime.now(),
    public val customerId: Long = 0,
    public val customerName: String = "",
    public val customerEmail: String? = null
)