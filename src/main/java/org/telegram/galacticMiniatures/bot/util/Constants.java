package org.telegram.galacticMiniatures.bot.util;

final public class Constants {

    private Constants() {}


    private static final String CLOSE = "\u2716  Close";
    private static final String ADD_CART = "\uD83D\uDCB0  Add";
    private static final String ADD_FAVORITE = "\u2B50  Add";
    private static final String NEXT = "\u276F";
    private static final String PREVIOUS = "\u276E";

    public static final String KEYBOARD_ORDER_HEADER = "\uD83D\uDC34  Orders";
    public static final String KEYBOARD_ORDER_OPERATED_CALLBACK = "OrderCallback";
    public static final String KEYBOARD_ORDER_BUTTON_CLOSE_COMMAND = "OrderCallbackClose";
    public static final String KEYBOARD_ORDER_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_ORDER_BUTTON_NEXT_COMMAND = "OrderCallbackNext";
    public static final String KEYBOARD_ORDER_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_ORDER_BUTTON_PREVIOUS_COMMAND = "OrderCallbackPrevious";
    public static final String KEYBOARD_ORDER_BUTTON_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_ORDER_BUTTON_CANCEL_ORDER_COMMAND = "OrderCallbackCancelOrder";
    public static final String KEYBOARD_ORDER_BUTTON_CANCEL_ORDER_NAME = "Cancel";
    public static final String KEYBOARD_ORDER_BUTTON_EDIT_COMMAND = "OrderCallbackEditOrder";
    public static final String KEYBOARD_ORDER_BUTTON_EDIT_NAME = "Edit";
    public static final String KEYBOARD_ORDER_BUTTON_TRACK_COMMAND = "OrderCallbackTrack";
    public static final String KEYBOARD_ORDER_BUTTON_TRACK_NAME = "Track";

    public static final String KEYBOARD_STARTER_SHOP_COMMAND = "\uD83E\uDDD9  Магазин";
    public static final String KEYBOARD_STARTER_INFORMATION_COMMAND = "\uD83D\uDCDC  Информация";
    public static final String KEYBOARD_STARTER_ADDRESS_COMMAND = "\uD83C\uDFF0  Адрес";
    public static final String KEYBOARD_STARTER_CART_COMMAND = "\uD83D\uDCB0  Корзина";
    public static final String KEYBOARD_STARTER_FAVORITE_COMMAND = "\u2B50  Избранное";
    public static final String KEYBOARD_STARTER_ORDER_COMMAND = "\uD83D\uDC34  Заказы";
    public static final String KEYBOARD_STARTER_FAVORITES_EMPTY = "Список пуст";
    public static final String KEYBOARD_STARTER_CART_EMPTY = "Корзина пуста";

    public static final String KEYBOARD_ADDRESS_OPERATED_CALLBACK = "AddressCallback";
    public static final String KEYBOARD_ADDRESS_BUTTON_EDIT_NAME = "\u270F  Edit";
    public static final String KEYBOARD_ADDRESS_BUTTON_EDIT_COMMAND = "AddressCallbackEdit";
    public static final String KEYBOARD_ADDRESS_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_ADDRESS_BUTTON_CLOSE_COMMAND = "AddressCallbackClose";
    public static final String QUERY_ADDRESS_QUESTION_1 = "\uD83E\uDDD9  Enter your full name";
    public static final String QUERY_ADDRESS_QUESTION_2 = "\uD83E\uDDD9  Enter your town";
    public static final String QUERY_ADDRESS_QUESTION_3 = "\uD83E\uDDD9  Enter your address";
    public static final String QUERY_ADDRESS_QUESTION_4 = "\uD83E\uDDD9  Enter your post index";

    public static final String KEYBOARD_LISTING_OPERATED_CALLBACK = "ListingCallback";
    public static final String KEYBOARD_LISTING_BUTTON_PREVIOUS_COMMAND = "ListingCallbackPrevious";
    public static final String KEYBOARD_LISTING_BUTTON_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_LISTING_BUTTON_GO_BACK_COMMAND = "ListingCallbackGoBack";
    public static final String KEYBOARD_LISTING_BUTTON_GO_BACK_NAME = "\u2B05  Back";
    public static final String KEYBOARD_LISTING_BUTTON_NEXT_COMMAND = "ListingCallbackNext";
    public static final String KEYBOARD_LISTING_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_LISTING_BUTTON_ADD_TO_FAVORITE_COMMAND = "ListingCallbackToFavorite";
    public static final String KEYBOARD_LISTING_BUTTON_ADD_TO_FAVORITE_NAME = ADD_FAVORITE;
    public static final String KEYBOARD_LISTING_BUTTON_ADD_TO_CART_COMMAND = "ListingCallbackToCart";
    public static final String KEYBOARD_LISTING_BUTTON_ADD_TO_CART_NAME = ADD_CART;
    public static final String KEYBOARD_LISTING_BUTTON_PHOTO_MIDDLE_COMMAND = "ListingCallback";
    public static final String KEYBOARD_LISTING_BUTTON_PHOTO_PREVIOUS_COMMAND = "ListingCallbackPreviousPhoto";
    public static final String KEYBOARD_LISTING_BUTTON_PHOTO_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_LISTING_BUTTON_PHOTO_NEXT_COMMAND = "ListingCallbackNextPhoto";
    public static final String KEYBOARD_LISTING_BUTTON_PHOTO_NEXT_NAME = NEXT;
    public static final String KEYBOARD_LISTING_BUTTON_OPTION_PREVIOUS_COMMAND = "ListingCallbackPreviousOption";
    public static final String KEYBOARD_LISTING_BUTTON_OPTION_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_LISTING_BUTTON_OPTION_NEXT_COMMAND = "ListingCallbackNextOption";
    public static final String KEYBOARD_LISTING_BUTTON_OPTION_NEXT_NAME = NEXT;

    public static final String KEYBOARD_FAVORITE_OPERATED_CALLBACK = "FavoriteCallback";
    public static final String KEYBOARD_FAVORITE_BUTTON_PREVIOUS_COMMAND = "FavoriteCallbackPrevious";
    public static final String KEYBOARD_FAVORITE_BUTTON_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_FAVORITE_BUTTON_EXIT_COMMAND = "FavoriteCallbackGoBack";
    public static final String KEYBOARD_FAVORITE_BUTTON_EXIT_NAME = CLOSE;
    public static final String KEYBOARD_FAVORITE_BUTTON_NEXT_COMMAND = "FavoriteCallbackNext";
    public static final String KEYBOARD_FAVORITE_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_FAVORITE_BUTTON_REMOVE_FROM_FAVORITE_COMMAND =
            "FavoriteCallbackDeleteFromFavorite";
    public static final String KEYBOARD_FAVORITE_BUTTON_REMOVE_FROM_FAVORITE_NAME = "\u2B50  Remove";
    public static final String KEYBOARD_FAVORITE_BUTTON_ADD_TO_CART_COMMAND = "FavoriteCallbackToCart";
    public static final String KEYBOARD_FAVORITE_BUTTON_ADD_TO_CART_NAME = ADD_CART;
    public static final String KEYBOARD_FAVORITE_BUTTON_PHOTO_MIDDLE_COMMAND = "FavoriteCallback";
    public static final String KEYBOARD_FAVORITE_BUTTON_PHOTO_PREVIOUS_COMMAND = "FavoriteCallbackPreviousPhoto";
    public static final String KEYBOARD_FAVORITE_BUTTON_PHOTO_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_FAVORITE_BUTTON_PHOTO_NEXT_COMMAND = "FavoriteCallbackNextPhoto";
    public static final String KEYBOARD_FAVORITE_BUTTON_PHOTO_NEXT_NAME = NEXT;
    public static final String KEYBOARD_FAVORITE_BUTTON_OPTION_PREVIOUS_COMMAND = "ListingCallbackPreviousOption";
    public static final String KEYBOARD_FAVORITE_BUTTON_OPTION_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_FAVORITE_BUTTON_OPTION_NEXT_COMMAND = "FavoriteCallbackNextOption";
    public static final String KEYBOARD_FAVORITE_BUTTON_OPTION_NEXT_NAME = NEXT;

    public static final String KEYBOARD_CART_OPERATED_CALLBACK = "CartCallback";
    public static final String KEYBOARD_CART_BUTTON_PREVIOUS_COMMAND = "CartCallbackPrevious";
    public static final String KEYBOARD_CART_BUTTON_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_CART_BUTTON_EXIT_COMMAND = "CartCallbackExit";
    public static final String KEYBOARD_CART_BUTTON_EXIT_NAME = CLOSE;
    public static final String KEYBOARD_CART_BUTTON_NEXT_COMMAND = "CartCallbackNext";
    public static final String KEYBOARD_CART_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_CART_BUTTON_REMOVE_FROM_CART_COMMAND = "CartCallbackDeleteFromCart";
    public static final String KEYBOARD_CART_BUTTON_REMOVE_FROM_CART_NAME = "\uD83D\uDCB0  Remove";
    public static final String KEYBOARD_CART_BUTTON_ADD_TO_FAVORITE_COMMAND = "CartCallbackToFavorite";
    public static final String KEYBOARD_CART_BUTTON_ADD_TO_FAVORITE_NAME = ADD_FAVORITE;
    public static final String KEYBOARD_CART_BUTTON_ADD_PLUS_NAME = "\u2795";
    public static final String KEYBOARD_CART_BUTTON_ADD_PLUS_COMMAND = "CartCallbackPlus";
    public static final String KEYBOARD_CART_BUTTON_ADD_MINUS_NAME = "\u2796";
    public static final String KEYBOARD_CART_BUTTON_ADD_MINUS_COMMAND = "CartCallbackMinus";
    public static final String KEYBOARD_CART_BUTTON_ORDER_COMMAND = "CartCallbackOrder";
    public static final String KEYBOARD_CART_BUTTON_ORDER_NAME = "Make Order";

    public static final String KEYBOARD_SECTIONS_HEADER = "\uD83E\uDDD9 Добро пожаловать в магазин!";
    public static final String KEYBOARD_SECTIONS_OPERATED_CALLBACK = "SectionsCallback";
    public static final String KEYBOARD_SECTIONS_BUTTON_CLOSE_COMMAND = "SectionsCallbackGoBack";
    public static final String KEYBOARD_SECTIONS_BUTTON_CLOSE_NAME = CLOSE;

    public static final String BOT_START = "Добро пожаловать! Этот бот магазина 3Д моделек";
    public static final String BOT_START_COMMAND = "/start";
    public static final String BOT_ABOUT = "This bot represents GalacticMiniatures store";
    public static final String BOT_ADDRESS_REQUEST = "Вы не заполнили адрес доставки. Пожалуйста сделайте это";

    public static final String ERROR_RESTART_MENU = "Unexpected error, please restart menu";
}