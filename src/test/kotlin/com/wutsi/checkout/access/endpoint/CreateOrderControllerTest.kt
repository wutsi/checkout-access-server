package com.wutsi.checkout.access.endpoint

import com.wutsi.checkout.access.dao.OrderDiscountRepository
import com.wutsi.checkout.access.dao.OrderItemDiscountRepository
import com.wutsi.checkout.access.dao.OrderItemRepository
import com.wutsi.checkout.access.dao.OrderRepository
import com.wutsi.checkout.access.dto.CreateOrderDiscountRequest
import com.wutsi.checkout.access.dto.CreateOrderItemRequest
import com.wutsi.checkout.access.dto.CreateOrderRequest
import com.wutsi.checkout.access.dto.CreateOrderResponse
import com.wutsi.checkout.access.enums.ChannelType
import com.wutsi.checkout.access.enums.DeviceType
import com.wutsi.checkout.access.enums.DiscountType
import com.wutsi.checkout.access.enums.OfferType
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.HttpStatus
import org.springframework.web.client.RestTemplate
import java.util.UUID
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CreateOrderControllerTest {
    @LocalServerPort
    public val port: Int = 0

    private val rest = RestTemplate()

    @Autowired
    private lateinit var dao: OrderRepository

    @Autowired
    private lateinit var itemDao: OrderItemRepository

    @Autowired
    private lateinit var discountDao: OrderDiscountRepository

    @Autowired
    private lateinit var itemDiscountDao: OrderItemDiscountRepository

    @Test
    public fun create() {
        val request = CreateOrderRequest(
            storeId = 11,
            customerId = 22,
            customerName = "Ray Sponsible",
            customerEmail = "ray.sponsible@gmail.com",
            deviceType = DeviceType.MOBILE.name,
            deviceId = UUID.randomUUID().toString(),
            deviceIp = "10.0.0.1",
            channelType = ChannelType.APP.name,
            notes = "This is the notes",
            currency = "XAF",
            discounts = listOf(
                CreateOrderDiscountRequest(
                    code = "X-111",
                    amount = 2000,
                    type = DiscountType.COUPON.name
                )
            ),
            items = listOf(
                CreateOrderItemRequest(
                    offerType = OfferType.PRODUCT.name,
                    offerId = 111,
                    price = 15000,
                    title = "Chemise",
                    pictureUrl = "https://www.img.1/111.png",
                    quantity = 3
                ),
                CreateOrderItemRequest(
                    offerType = OfferType.PRODUCT.name,
                    offerId = 222,
                    price = 10000,
                    title = "Chemise",
                    quantity = 1,
                    discounts = listOf(
                        CreateOrderDiscountRequest(
                            code = "SPECIAL",
                            amount = 1000,
                            type = DiscountType.MERCHANT.name
                        )
                    )
                )
            )
        )
        val response = rest.postForEntity(url(), request, CreateOrderResponse::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)

        val orderId = response.body!!.orderId
        val order = dao.findById(orderId).get()

        assertEquals(request.storeId, order.storeId)
        assertEquals(request.customerId, order.customerId)
        assertEquals(request.customerEmail, order.customerEmail)
        assertEquals(request.customerName, order.customerName)
        assertEquals(DeviceType.MOBILE, order.deviceType)
        assertEquals(request.deviceIp, order.deviceIp)
        assertEquals(request.deviceId, order.deviceId)
        assertEquals(ChannelType.APP, order.channelType)
        assertEquals(request.notes, order.notes)
        assertEquals(request.currency, order.currency)
        assertEquals(3000, order.totalDiscount)
        assertEquals(55000, order.subTotalPrice)
        assertEquals(52000, order.totalPrice)

        val discounts = discountDao.findByOrder(order)
        assertEquals(1, discounts.size)
        assertEquals(request.discounts[0].code, discounts[0].code)
        assertEquals(request.discounts[0].amount, discounts[0].amount)

        val items = itemDao.findByOrder(order)
        assertEquals(2, items.size)

        assertEquals(request.items[0].quantity, items[0].quantity)
        assertEquals(request.items[0].offerId, items[0].offerId)
        assertEquals(OfferType.valueOf(request.items[0].offerType), items[0].offerType)
        assertEquals(request.items[0].price, items[0].unitPrice)
        assertEquals(request.items[0].title, items[0].title)
        assertEquals(request.items[0].pictureUrl, items[0].pictureUrl)
        assertEquals(45000L, items[0].subTotalPrice)
        assertEquals(0, items[0].totalDiscount)
        assertEquals(45000L, items[0].totalPrice)

        assertEquals(request.items[1].quantity, items[1].quantity)
        assertEquals(request.items[1].offerId, items[1].offerId)
        assertEquals(OfferType.valueOf(request.items[1].offerType), items[1].offerType)
        assertEquals(request.items[1].price, items[1].unitPrice)
        assertEquals(request.items[1].title, items[1].title)
        assertEquals(request.items[1].pictureUrl, items[1].pictureUrl)
        assertEquals(10000L, items[1].subTotalPrice)
        assertEquals(1000, items[1].totalDiscount)
        assertEquals(9000L, items[1].totalPrice)

        val itemDiscounts = itemDiscountDao.findByOrderItem(items[1])
        assertEquals(1, itemDiscounts.size)
        assertEquals(request.items[1].discounts[0].code, itemDiscounts[0].code)
        assertEquals(request.items[1].discounts[0].amount, itemDiscounts[0].amount)
    }

    private fun url() = "http://localhost:$port/v1/orders"
}