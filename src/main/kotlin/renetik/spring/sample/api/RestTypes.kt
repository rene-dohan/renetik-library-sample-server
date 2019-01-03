package renetik.spring.sample.api

data class Response(val success: Boolean, val message: String? = null)

fun restOperation(function: () -> Any) = try {
    function()
} catch (ex: Throwable) {
    Response(false, "Exception on server: $ex")
}