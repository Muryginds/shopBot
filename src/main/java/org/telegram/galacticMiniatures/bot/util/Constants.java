package org.telegram.galacticMiniatures.bot.util;

final public class Constants {

    private Constants() {}




    private static final String CLOSE = "\u2716  Close";
    private static final String ADD_CART = "\uD83D\uDCB0  Add";
    private static final String ADD_FAVORITE = "\u2B50  Add";
    private static final String NEXT = "\u276F";
    private static final String PREVIOUS = "\u276E";
    private static final String MINUS = "\u2796";
    private static final String PLUS = "\u2795";
    private static final String BACK = "\u2B05  Back";

    public static final String KEYBOARD_ADMIN_MESSAGES_OPERATED_CALLBACK = "AdminMessagesCallback";
    public static final String KEYBOARD_ADMIN_MESSAGES_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_ADMIN_MESSAGES_BUTTON_CLOSE_COMMAND = "AdminMessagesCallbackClose";
    public static final String KEYBOARD_ADMIN_MESSAGES_BUTTON_MESSAGES_COMMAND = "AdminMessagesCallbackMessages";

    public static final String KEYBOARD_USER_CHAT_MESSAGE_OPERATED_CALLBACK = "UserChatMessageCallback";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_CLOSE_COMMAND = "UserChatMessageCallbackClose";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_ADD_MESSAGE_COMMAND = "UserChatMessageCallbackAddMessage";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_ADD_MESSAGE_NAME = "Add message";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_NEXT_COMMAND = "UserChatMessageCallbackNext";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_PREVIOUS_COMMAND = "UserChatMessageCallbackPrevious";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_PREVIOUS_NAME = PREVIOUS;

    public static final String KEYBOARD_USER_ORDER_MESSAGE_OPERATED_CALLBACK = "UserOrderMessageCallback";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_CLOSE_COMMAND = "UserOrderMessageCallbackClose";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_ADD_MESSAGE_COMMAND = "UserOrderMessageCallbackAddMessage";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_ADD_MESSAGE_NAME = "Add message";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_NEXT_COMMAND = "UserOrderMessageCallbackNext";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_PREVIOUS_COMMAND = "UserOrderMessageCallbackPrevious";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_PREVIOUS_NAME = PREVIOUS;
    public static final String QUERY_ADD_MESSAGE_WARNING = "Send your message. To cancel entry send text \"NO\"";
    public static final String QUERY_ADD_MESSAGE_CANCEL = "NO";

    public static final String KEYBOARD_ADMIN_OPERATED_CALLBACK = "AdminPanelCallback";
    public static final String KEYBOARD_ADMIN_BUTTON_PROMOTE_USER_COMMAND = "AdminPanelCallbackPromote";
    public static final String KEYBOARD_ADMIN_BUTTON_PROMOTE_USER_NAME = "Promote user to admin";
    public static final String KEYBOARD_ADMIN_BUTTON_DEMOTE_USER_COMMAND = "AdminPanelCallbackDemote";
    public static final String KEYBOARD_ADMIN_BUTTON_DEMOTE_USER_NAME = "Demote admin to user";
    public static final String KEYBOARD_ADMIN_ERROR_NO_RIGHTS = "You have no rights to do that";
    public static final String KEYBOARD_ADMIN_ERROR_USER_NOT_FOUND = "User not found";
    public static final String KEYBOARD_ADMIN_BUTTON_CLOSE_MENU_COMMAND = "AdminPanelCallbackCloseMenu";
    public static final String KEYBOARD_ADMIN_BUTTON_CLOSE_MENU_NAME = CLOSE;
    public static final String QUERY_ADMIN_QUESTION_ENTER_CHAT_ID = "Enter user Chat ID";

    public static final String KEYBOARD_ORDEREDLISTING_OPERATED_CALLBACK = "OrderedListingCallBack";
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_PREVIOUS_COMMAND = "OrderedListingCallBackPrevious";
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_NEXT_COMMAND = "OrderedListingCallBackBack";
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_ADD_MINUS_COMMAND = "OrderedListingCallBackMinus";
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_ADD_MINUS_NAME = MINUS;
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_ADD_PLUS_COMMAND = "OrderedListingCallBackPlus";
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_ADD_PLUS_NAME = PLUS;
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_REMOVE_FROM_ORDER_COMMAND =
            "OrderedListingCallBackRemoveFromOrder";
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_REMOVE_FROM_ORDER_NAME = "Remove from Order";
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_GO_BACK_COMMAND = "OrderedListingCallBackGoBack";
    public static final String KEYBOARD_ORDEREDLISTING_BUTTON_GO_BACK_NAME = BACK;
    public static final String KEYBOARD_ORDEREDLISTING_MESSAGE_ORDER_IS_EMPTY ="Order is empty. Deleting order";

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
    public static final String KEYBOARD_ORDER_BUTTON_MESSAGES_COMMAND = "OrderCallbackMessages";
    public static final String KEYBOARD_ORDER_BUTTON_MESSAGES_NAME = "Messages";

    public static final String KEYBOARD_STARTER_SHOP_COMMAND = "\uD83E\uDDD9  Магазин";
    public static final String KEYBOARD_STARTER_INFORMATION_COMMAND = "\uD83D\uDCDC  Информация";
    public static final String KEYBOARD_STARTER_ADDRESS_COMMAND = "\uD83C\uDFF0  Адрес";
    public static final String KEYBOARD_STARTER_CART_COMMAND = "\uD83D\uDCB0  Корзина";
    public static final String KEYBOARD_STARTER_FAVORITE_COMMAND = "\u2B50  Избранное";
    public static final String KEYBOARD_STARTER_ORDER_COMMAND = "\uD83D\uDC34  Заказы";
    public static final String KEYBOARD_STARTER_MESSAGES_COMMAND = "Отправить сообщение";
    public static final String KEYBOARD_STARTER_ADMIN_PANEL_COMMAND = "Панель администратора";
    public static final String KEYBOARD_STARTER_ADMIN_MESSAGES_COMMAND = "Сообщения пользователей";
    public static final String KEYBOARD_STARTER_USER_MESSAGES_COMMAND = "Центр сообщений";
    public static final String KEYBOARD_STARTER_FAVORITES_EMPTY = "Список пуст";
    public static final String KEYBOARD_STARTER_CART_EMPTY = "Корзина пуста";
    public static final String KEYBOARD_STARTER_ORDERS_EMPTY = "Список заказов пуст";

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
    public static final String KEYBOARD_LISTING_BUTTON_GO_BACK_NAME = BACK;
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
    public static final String KEYBOARD_FAVORITE_LIST_EMPTY = "Список пуст";

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
    public static final String KEYBOARD_CART_BUTTON_ADD_PLUS_NAME = PLUS;
    public static final String KEYBOARD_CART_BUTTON_ADD_PLUS_COMMAND = "CartCallbackPlus";
    public static final String KEYBOARD_CART_BUTTON_ADD_MINUS_NAME = MINUS;
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