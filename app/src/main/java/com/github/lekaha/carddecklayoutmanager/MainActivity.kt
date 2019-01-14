package com.github.lekaha.carddecklayoutmanager

import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.lekaha.views.CardDeckLayoutManager
import kotlinx.android.synthetic.main.activity_main.recycler

class MainActivity : AppCompatActivity() {
    companion object {
        const val CARDS_SIZE = 18
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var cardSize = CARDS_SIZE
        val sequence = generateSequence { randomGenerateCard().takeIf { --cardSize > 0 } }
        val cards = sequence.toList()
        recycler.adapter = Adapter(cards)

        val revealHeight = resources.toPixel(66f)
        recycler.layoutManager = CardDeckLayoutManager(this, recycler, revealHeight)
    }

    private fun randomGenerateCard() =
        Card(
            CardSuit.values().let {
                it[kotlin.random.Random.nextInt(it.size)]
            },
            kotlin.random.Random.nextInt(1, 14)
        )
}

class Adapter(val cards: List<Card>): RecyclerView.Adapter<CardViewHolder>() {
    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return CardViewHolder(view)
    }
    override fun getItemCount() = cards.size
}

class CardViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val icon by lazy { itemView.findViewById<ImageView>(R.id.icon) }
    private val number by lazy { itemView.findViewById<TextView>(R.id.number) }
    private val face by lazy { itemView.findViewById<ImageView>(R.id.face) }
    fun bind(card: Card) {
        val (suitResId, faceStringFormat) =
            when (card.suit) {
                CardSuit.SPADE -> Pair(R.drawable.spade, "card%ss")
                CardSuit.HEARD -> Pair(R.drawable.heart, "card%sh")
                CardSuit.DIAMOND -> Pair(R.drawable.diamond, "card%sd")
                CardSuit.CLUB -> Pair(R.drawable.club, "card%sc")
            }

        icon.setImageResource(suitResId)
        number.text =
                when (card.number) {
                    1 -> "A"
                    11 -> "J"
                    12 -> "Q"
                    13 -> "K"
                    else -> card.number.toString()
                }
        val faceResId = itemView.resources.getIdentifier(
            String.format(faceStringFormat, number.text.toString().toLowerCase()),
            "drawable",
            itemView.context.packageName
        )
        face.setImageResource(faceResId)
    }
}

enum class CardSuit {
    SPADE, HEARD, DIAMOND, CLUB
}

data class Card(
    val suit: CardSuit,
    val number: Int
)

fun Resources.toPixel(dp: Float) =
    TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics).toInt()
