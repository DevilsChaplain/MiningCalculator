package app.beer.coin_galaxy.utils

/**
 * Используется в качестве оболочки для данных,
 * которые отображаются через текущие данные, представляющие событие.
 */
class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Разрешить внешнее чтение, но не запись

    /**
     * Возвращает содержимое и предотвращает его повторное использование.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Возвращает содержимое, даже если оно уже было обработано.
     */
    fun peekContent(): T = content

    override fun toString(): String {
        return "$hasBeenHandled, ${getContentIfNotHandled()}"
    }

}
