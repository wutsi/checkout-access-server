package com.wutsi.checkout.access.endpoint

import com.wutsi.checkout.access.`delegate`.SearchOrdersDelegate
import com.wutsi.checkout.access.dto.SearchOrderRequest
import com.wutsi.checkout.access.dto.SearchOrderResponse
import org.springframework.web.bind.`annotation`.PostMapping
import org.springframework.web.bind.`annotation`.RequestBody
import org.springframework.web.bind.`annotation`.RestController
import javax.validation.Valid

@RestController
public class SearchOrdersController(
    public val `delegate`: SearchOrdersDelegate
) {
    @PostMapping("/v1/orders/search")
    public fun invoke(@Valid @RequestBody request: SearchOrderRequest): SearchOrderResponse =
        delegate.invoke(request)
}
