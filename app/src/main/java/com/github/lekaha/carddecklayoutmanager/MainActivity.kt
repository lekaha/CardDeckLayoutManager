package com.github.lekaha.carddecklayoutmanager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.recycler

class MainActivity : AppCompatActivity() {
    companion object {
        const val CARDS_SIZE = 11
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var cardSize = CARDS_SIZE
        val sequence = generateSequence { randomGenerateCard().takeIf { --cardSize > 0 } }
        val cards = sequence.toList()
        recycler.adapter = Adapter(cards)

        // FIXME: use custom layout manager
        recycler.layoutManager = LinearLayoutManager(this)
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
    fun bind(card: Card) {
        icon.setImageResource(
            when (card.suit) {
                CardSuit.SPADE -> R.drawable.spade
                CardSuit.HEARD -> R.drawable.heart
                CardSuit.DIAMOND -> R.drawable.diamond
                CardSuit.CLUB -> R.drawable.club
            }
        )
        number.text =
                when (card.number) {
                    1 -> "A"
                    11 -> "J"
                    12 -> "Q"
                    13 -> "K"
                    else -> card.number.toString()
                }
    }
}

enum class CardSuit {
    SPADE, HEARD, DIAMOND, CLUB
}

data class Card(
    val suit: CardSuit,
    val number: Int
)
