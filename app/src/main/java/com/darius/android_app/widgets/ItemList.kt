package com.darius.android_app.widgets

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.darius.android_app.R
import com.darius.android_app.model.Hotel
import java.util.*

typealias OnItemFn = (id: String?) -> Unit

@Composable
fun ItemList(hotels: List<Hotel>, onItemClick: OnItemFn) {
    LazyColumn {
        items(hotels) { hotel ->
            ItemDetail(hotel = hotel, onItemClick = onItemClick)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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
            if (hotel.imageUri?.isNotEmpty() == true) {
                val b = Base64.getDecoder().decode(hotel.imageUri)
                val imageBitmap = BitmapFactory.decodeByteArray(b, 0, b.size)
                Image(
                    bitmap = imageBitmap.asImageBitmap(),
                    contentDescription = "---",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding()
                        .padding(5.dp)
                )
            }
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