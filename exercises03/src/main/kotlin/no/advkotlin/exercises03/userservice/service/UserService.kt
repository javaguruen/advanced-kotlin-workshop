package no.advkotlin.exercises03.userservice.service

import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import no.advkotlin.exercises03.userservice.domain.Product
import no.advkotlin.exercises03.userservice.domain.User

class UserService {

    suspend fun getUser(userId: String): User = coroutineScope {
        delay(1000L)
        users.filter { it.userId.equals(userId) }.first()
    }

    suspend fun getProducts(userId: String): List<Product> = coroutineScope {
        delay(1000L)
        userItems.get(userId)!!.map { id -> items.filter { it.productId == id }.first() }
    }

}

val users = listOf(
    User("100", "Tiril Espensen"),
    User("101", "Signy Evensen"),
    User("102", "Andrine Solberg"),
    User("103", "Louise Oliversen"),
    User("104", "Sigurd Ruud"),
    User("105", "Haldor Holt"),
    User("106", "Bernhard Thorsen"),
    User("107", "Ernst Iversen"),
    User("108", "Torhild Abrahamsen"),
    User("109", "Stein Christiansen"),
    User("110", "Jesper Wolff"),
    User("111", "Isak Jensen"),
    User("112", "Snorre Rolvsson"),
    User("113", "Nathalie Paulsen"),
    User("114", "Nanna Carlsen"),
)

val items = listOf(
    Product("2000", "Mantle of Mischief", 100),
    Product("2001", "Jar of Stolen Souls", 100),
    Product("2002", "Evermeet’s Endless Quiver", 100),
    Product("2003", "Alchemist’s Chalice", 100),
    Product("2004", "Singing Sword of Warning", 100),
    Product("2005", "Law-Abiding Quiver", 100),
    Product("2006", "Herbacious Candle of Seduction", 100),
    Product("2007", "Sphynx’s Paw", 100),
    Product("2008", "Critter Creator", 100),
    Product("2009", "Watchful Statuette", 100),
    Product("2010", "Enchanted Voicebox of Beasts", 100),
    Product("2011", "Greaves of the Great Desert", 100),
)

val userItems = mapOf(
    "100" to listOf("2010", "2003"),
    "101" to listOf("2002"),
    "102" to listOf("2001", "2010"),
    "103" to listOf("2005"),
    "104" to listOf("2002"),
    "105" to listOf("2004"),
    "106" to listOf("2005"),
    "107" to listOf("2011", "2005"),
    "108" to listOf("2007"),
    "109" to listOf("2008"),
    "110" to listOf("2011"),
    "111" to listOf("2010"),
    "112" to listOf("2011"),
    "113" to listOf(),
    "114" to listOf(),
)