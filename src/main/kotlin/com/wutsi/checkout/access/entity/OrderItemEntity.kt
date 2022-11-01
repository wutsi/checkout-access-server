package com.wutsi.checkout.access.entity

import com.wutsi.checkout.access.enums.OfferType
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "T_ORDER_ITEM")
data class OrderItemEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val offerId: Long = -1,
    val offerType: OfferType = OfferType.UNKNOWN,
    val title: String = "",
    val pictureUrl: String? = null,
    val unitPrice: Long = 0L,
    var totalPrice: Long = 0L,
    val subTotalPrice: Long = 0L,
    val totalDiscount: Long = 0L,
    val quantity: Int = 1,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_fk")
    val order: OrderEntity = OrderEntity(),

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "orderItem")
    val discounts: List<OrderItemDiscountEntity> = emptyList()
)