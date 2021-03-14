package com.example.rememberstate

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.example.rememberstate.ui.theme.RememberStateTheme
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RememberStateTheme {
                val navController = rememberNavController()

                NavHost(navController, startDestination = "items") {
                    composable("items") {
                        Greeting(
                            onItemClick = { navController.navigate("details/$it") }
                        )
                    }
                    composable(
                        "details/{index}",
                        arguments = listOf(navArgument("index") { type = NavType.IntType })
                    ) { backStackEntry ->
                        DetailsScreen(backStackEntry.arguments?.getInt("index") ?: -1)
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(
    onItemClick: (Int) -> Unit
) {
    val items = remember { (0..100).toList() }

    Surface(color = MaterialTheme.colors.background) {
        val states = items.map { remember(it) { mutableStateOf(ItemState()) } }

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(items) { item ->
                val state = rememberSaveable(item) { mutableStateOf(ItemState()) }

                key(item) {
                    Item(index = item,
                        state = state.value,
                        onClick = { onItemClick(item) },
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                state.value.changeState()
                            }
                    )
                }
                Divider()
            }
        }
    }
}

@Composable
fun Item(
    index: Int,
    state: ItemState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.Center)
        ) {
            Text(
                text = index.toString(),
                modifier = Modifier.padding(16.dp)

            )

            if (state.expanded) {
                Button(
                    onClick = onClick,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Click me")
                }
            }
        }
    }
}

@Parcelize
class ItemState : Parcelable {

    val expanded: Boolean
        get() = _expanded.value

    @IgnoredOnParcel
    private val _expanded = mutableStateOf(false)

    fun changeState() {
        _expanded.value = !_expanded.value
    }
}

@Composable
fun DetailsScreen(
    index: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Gray.copy(alpha = 0.3f))
    ) {
        Text(
            text = index.toString(),
            modifier = Modifier.align(Alignment.Center)
        )
    }
}