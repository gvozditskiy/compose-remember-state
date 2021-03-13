
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



    LazyColumn(modifier = Modifier.fillMaxSize()) {
    items(items) { item ->
    val state = states[item]

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
        

If we create state like val state = remember(it) { mutableStateOf(ItemState()) }
then we loose expanded state while scrolling
If we lift up states generating higher before LazyColumn then expand state
is saving properly

But when we expand an item, click the button and then go back to items we loose
expanded state
