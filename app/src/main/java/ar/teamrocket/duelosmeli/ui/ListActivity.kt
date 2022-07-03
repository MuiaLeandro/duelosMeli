package ar.teamrocket.duelosmeli.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import ar.teamrocket.duelosmeli.data.model.ItemPlayed
import coil.compose.AsyncImage
import java.util.ArrayList

class ListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val listItems = intent.extras?.getParcelableArrayList<ItemPlayed>("items")
        setContent {
            if (listItems != null) {
                cardList(listItems)
            }
        }
    }

    @Composable
    fun cardList(lista: ArrayList<ItemPlayed>) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 8.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.background(Color.LightGray)
        ) {
            items(lista.toList()) { item ->
                cardRow(
                    itemPlayed = item
                )
            }
        }
    }

    @Composable
    fun cardRow(itemPlayed: ItemPlayed) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = itemPlayed.title,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.size(8.dp))
                Divider(
                    color = Color.LightGray
                )
                AsyncImage(
                    model = itemPlayed.picture, contentDescription = null, modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Divider(
                    color = Color.LightGray
                )
                Spacer(Modifier.size(8.dp))
                Text(
                    text = "Ver en Mercado Libre",
                    color = Color.Blue,
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        //TODO abrir la publicación en Mercado Libre
                        // itemPlayed.link

                        }
                )
            }
        }
    }
}