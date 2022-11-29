package com.darius.android_app.item.ui.items

import android.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Card
import androidx.compose.material.CheckboxDefaults.colors
import androidx.compose.material.Colors
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.darius.android_app.R
import com.darius.android_app.item.data.Hotel

typealias OnItemFn = (id: String?) -> Unit

@Composable
fun ItemList(hotels: List<Hotel>, onItemClick: OnItemFn) {
    LazyColumn {
        items(hotels) { hotel ->
            ItemDetail(hotel = hotel, onItemClick = onItemClick)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ItemDetail(hotel: Hotel, onItemClick: OnItemFn) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        onClick = { onItemClick(hotel._id) }
    ) {
        Column() {
            AsyncImage(
                model = hotel.imageURL,
                contentDescription = "---",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding()
                    .padding(5.dp)
            )
            Box(modifier = Modifier.height(10.dp))
            Text(
                text = hotel.name,
                style = TextStyle(
                    color = colorResource(id = R.color.purple_700),
                ),
                fontSize = 24.sp,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Box(modifier = Modifier.height(10.dp))
            Text(
                text = hotel.price.toString(),
                modifier = Modifier.padding(horizontal = 10.dp)
            )
            Box(modifier = Modifier.height(10.dp))
        }
    }
}