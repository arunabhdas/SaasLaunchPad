package app.saaslaunchpad.saaslaunchpadapp.domain.model

data class CoinPaprikaApiResponseItem(
    val id: String,
    val is_active: Boolean,
    val is_new: Boolean,
    val name: String,
    val rank: Int,
    val symbol: String,
    val type: String
)