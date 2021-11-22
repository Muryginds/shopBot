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
    private static final String ADD_MESSAGE = "Add message";

    public static final String KEYBOARD_STARTER_SHOP_COMMAND = "\uD83E\uDDD9  Магазин";
    public static final String KEYBOARD_STARTER_INFORMATION_COMMAND = "\uD83D\uDCDC  Информация";
    public static final String KEYBOARD_STARTER_ADDRESS_COMMAND = "\uD83C\uDFF0  Адрес";
    public static final String KEYBOARD_STARTER_CART_COMMAND = "\uD83D\uDCB0  Корзина";
    public static final String KEYBOARD_STARTER_FAVORITE_COMMAND = "\u2B50  Избранное";
    public static final String KEYBOARD_STARTER_ORDER_COMMAND = "\uD83D\uDC34  Заказы";
    public static final String KEYBOARD_STARTER_ADMIN_PANEL_COMMAND = "Панель администратора";
    public static final String KEYBOARD_STARTER_MODERATOR_MESSAGES_COMMAND = "Центр сообщений";
    public static final String KEYBOARD_STARTER_MODERATOR_ORDERS_COMMAND = "Контроль заказов";
    public static final String KEYBOARD_STARTER_USER_MESSAGES_COMMAND = "Сообщения";
    public static final String KEYBOARD_STARTER_FAVORITES_EMPTY = "Список пуст";
    public static final String KEYBOARD_STARTER_CART_EMPTY = "Корзина пуста";
    public static final String KEYBOARD_STARTER_ORDERS_EMPTY = "Список заказов пуст";

    public static final String KEYBOARD_USER_CHAT_MESSAGE_OPERATED_CALLBACK = "UserChatMessageCallback";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_CLOSE_COMMAND = "UserChatMessageCallbackClose";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_ADD_MESSAGE_COMMAND =
            "UserChatMessageCallbackAddMessage";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_ADD_MESSAGE_NAME = ADD_MESSAGE;
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_NEXT_COMMAND = "UserChatMessageCallbackNext";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_PREVIOUS_COMMAND = "UserChatMessageCallbackPrevious";
    public static final String KEYBOARD_USER_CHAT_MESSAGE_BUTTON_PREVIOUS_NAME = PREVIOUS;

    public static final String KEYBOARD_USER_MESSAGES_OPERATED_CALLBACK = "UserMessagesCallback";
    public static final String KEYBOARD_USER_MESSAGES_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_USER_MESSAGES_BUTTON_CLOSE_COMMAND = "UserMessagesCallbackClose";
    public static final String KEYBOARD_USER_MESSAGES_BUTTON_MESSAGES_COMMAND = "UserMessagesCallbackMessages";

    public static final String KEYBOARD_MODERATOR_MESSAGES_OPERATED_CALLBACK = "ModeratorMessagesCallback";
    public static final String KEYBOARD_MODERATOR_MESSAGES_HEADER = "Messages control panel";
    public static final String KEYBOARD_MODERATOR_MESSAGES_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_MODERATOR_MESSAGES_BUTTON_CLOSE_COMMAND = "ModeratorMessagesCallbackClose";
    public static final String KEYBOARD_MODERATOR_MESSAGES_BUTTON_MESSAGES_COMMAND =
            "ModeratorMessagesCallbackMessages";
    public static final String KEYBOARD_MODERATOR_MESSAGES_BUTTON_NEXT_COMMAND =
            "ModeratorMessagesCallbackNext";
    public static final String KEYBOARD_MODERATOR_MESSAGES_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_MODERATOR_MESSAGES_BUTTON_PREVIOUS_COMMAND =
            "ModeratorMessagesCallbackPrevious";
    public static final String KEYBOARD_MODERATOR_MESSAGES_BUTTON_PREVIOUS_NAME = PREVIOUS;

    public static final String KEYBOARD_MODERATOR_MESSAGE_SCROLLER_OPERATED_CALLBACK =
            "ModeratorMessageScrollerCallback";
    public static final String KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_CLOSE_COMMAND =
            "ModeratorMessageScrollerCallbackClose";
    public static final String KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_ADD_MESSAGE_COMMAND =
            "ModeratorMessageScrollerCallbackAddMessage";
    public static final String KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_ADD_MESSAGE_NAME = ADD_MESSAGE;
    public static final String KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_NEXT_COMMAND =
            "ModeratorMessageScrollerCallbackNext";
    public static final String KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_PREVIOUS_COMMAND =
            "ModeratorMessageScrollerCallbackPrevious";
    public static final String KEYBOARD_MODERATOR_MESSAGE_SCROLLER_BUTTON_PREVIOUS_NAME = PREVIOUS;

    public static final String KEYBOARD_USER_MESSAGE_SCROLLER_OPERATED_CALLBACK = "UserMessageScrollerCallback";
    public static final String KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_PREVIOUS_COMMAND =
            "UserMessageScrollerCallbackPrevious";
    public static final String KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_NEXT_COMMAND = "UserMessageScrollerCallbackNext";
    public static final String KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_ADD_MESSAGE_COMMAND =
            "UserMessageScrollerCallbackAddMessage";
    public static final String KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_ADD_MESSAGE_NAME = ADD_MESSAGE;
    public static final String KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_CLOSE_COMMAND = "UserMessageScrollerCallbackClose";
    public static final String KEYBOARD_USER_MESSAGE_SCROLLER_BUTTON_CLOSE_NAME = CLOSE;

    public static final String KEYBOARD_USER_ORDER_MESSAGE_OPERATED_CALLBACK = "UserOrderMessageCallback";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_CLOSE_COMMAND = "UserOrderMessageCallbackClose";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_ADD_MESSAGE_COMMAND =
            "UserOrderMessageCallbackAddMessage";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_ADD_MESSAGE_NAME = ADD_MESSAGE;
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_NEXT_COMMAND = "UserOrderMessageCallbackNext";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_PREVIOUS_COMMAND = "UserOrderMessageCallbackPrevious";
    public static final String KEYBOARD_USER_ORDER_MESSAGE_BUTTON_PREVIOUS_NAME = PREVIOUS;
    public static final String QUERY_ADD_MESSAGE_WARNING = "Send your message. To cancel entry send text \"NO\"";
    public static final String QUERY_ADD_MESSAGE_CANCEL = "NO";

    public static final String KEYBOARD_ADMIN_OPERATED_CALLBACK = "AdminPanelCallback";
    public static final String KEYBOARD_ADMIN_BUTTON_PROMOTE_ADMIN_COMMAND = "AdminPanelCallbackPromoteAdmin";
    public static final String KEYBOARD_ADMIN_BUTTON_PROMOTE_ADMIN_NAME = "Promote user to admin";
    public static final String KEYBOARD_ADMIN_BUTTON_DEMOTE_ADMIN_COMMAND = "AdminPanelCallbackDemoteAdmin";
    public static final String KEYBOARD_ADMIN_BUTTON_DEMOTE_ADMIN_NAME = "Demote admin to user";
    public static final String KEYBOARD_ADMIN_ERROR_NO_RIGHTS = "You have no rights to do that";
    public static final String KEYBOARD_ADMIN_ERROR_USER_NOT_FOUND = "User not found";
    public static final String KEYBOARD_ADMIN_BUTTON_PROMOTE_MODERATOR_NAME = "Promote user to moderator";
    public static final String KEYBOARD_ADMIN_BUTTON_PROMOTE_MODERATOR_COMMAND = "AdminPanelCallbackPromoteModerator";
    public static final String KEYBOARD_ADMIN_BUTTON_DEMOTE_MODERATOR_NAME = "Demote moderator to user";
    public static final String KEYBOARD_ADMIN_BUTTON_DEMOTE_MODERATOR_COMMAND = "AdminPanelCallbackDemoteModerator";
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

    public static final String KEYBOARD_USER_ORDER_OPERATED_CALLBACK = "UserOrderCallback";
    public static final String KEYBOARD_USER_ORDER_BUTTON_CLOSE_COMMAND = "UserOrderCallbackClose";
    public static final String KEYBOARD_USER_ORDER_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_USER_ORDER_BUTTON_NEXT_COMMAND = "UserOrderCallbackNext";
    public static final String KEYBOARD_USER_ORDER_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_USER_ORDER_BUTTON_PREVIOUS_COMMAND = "UserOrderCallbackPrevious";
    public static final String KEYBOARD_USER_ORDER_BUTTON_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_USER_ORDER_BUTTON_CANCEL_ORDER_COMMAND = "UserOrderCallbackCancelOrder";
    public static final String KEYBOARD_USER_ORDER_BUTTON_CANCEL_ORDER_NAME = "Cancel";
    public static final String KEYBOARD_USER_ORDER_BUTTON_EDIT_COMMAND = "UserOrderCallbackEditOrder";
    public static final String KEYBOARD_USER_ORDER_BUTTON_EDIT_NAME = "Edit";
    public static final String KEYBOARD_USER_ORDER_BUTTON_TRACK_COMMAND = "UserOrderCallbackTrack";
    public static final String KEYBOARD_USER_ORDER_BUTTON_TRACK_NAME = "Track";
    public static final String KEYBOARD_USER_ORDER_BUTTON_MESSAGES_COMMAND = "UserOrderCallbackMessages";
    public static final String KEYBOARD_USER_ORDER_BUTTON_MESSAGES_NAME = "Messages";

    public static final String KEYBOARD_MODERATOR_ORDER_EDIT_OPERATED_CALLBACK = "ModeratorOrderEditCallback";
    public static final String KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CLOSE_COMMAND = "ModeratorOrderEditCallbackClose";
    public static final String KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CHANGE_STATUS_COMMAND =
            "ModeratorOrderEditCallbackChangeStatus";
    public static final String KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CHANGE_STATUS_NAME = "Edit status";
    public static final String KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CHANGE_TRACK_NUMBER_COMMAND =
            "ModeratorOrderEditCallbackChangeTrack";
    public static final String KEYBOARD_MODERATOR_ORDER_EDIT_BUTTON_CHANGE_TRACK_NUMBER_NAME = "Edit track number";

    public static final String KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_OPERATED_CALLBACK =
            "ModeratorOrderStatusEditCallback";
    public static final String KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_CHANGE_STATUS_COMMAND =
            "ModeratorOrderStatusEditCallbackChangeStatus";
    public static final String KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_CLOSE_COMMAND =
            "ModeratorOrderStatusEditCallbackClose";
    public static final String KEYBOARD_MODERATOR_ORDER_STATUS_EDIT_CLOSE_NAME = CLOSE;

    public static final String KEYBOARD_MODERATOR_ORDERS_OPERATED_CALLBACK = "ModeratorOrdersCallback";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_CLOSE_COMMAND = "ModeratorOrdersCallbackClose";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_NEXT_COMMAND = "ModeratorOrdersCallbackNext";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_NEXT_NAME = NEXT;
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_PREVIOUS_COMMAND = "ModeratorOrdersCallbackPrevious";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_PREVIOUS_NAME = PREVIOUS;
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_NEW_COMMAND = "ModeratorOrdersCallbackNew";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_NEW_NAME = "New";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_CONFIRMED_COMMAND = "ModeratorOrdersCallbackConfirmed";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_CONFIRMED_NAME = "Confirmed";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_PAID_COMMAND = "ModeratorOrdersCallbackPaid";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_PAID_NAME = "Paid";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_PRINTING_COMMAND = "ModeratorOrdersCallbackPrinting";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_PRINTING_NAME = "Printing";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_CANCELED_COMMAND = "ModeratorOrdersCallbackCanceled";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_CANCELED_NAME = "Canceled";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_ORDERS_COMMAND = "ModeratorOrdersCallbackOrders";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_ALL_STATUSES_COMMAND =
            "ModeratorOrdersCallbackAllStatuses";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_ALL_STATUSES_NAME = "All";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_SHIPPED_COMMAND = "ModeratorOrdersCallbackShipped";
    public static final String KEYBOARD_MODERATOR_ORDERS_BUTTON_SHIPPED_NAME = "Shipped";

    public static final String KEYBOARD_ADDRESS_OPERATED_CALLBACK = "AddressCallback";
    public static final String KEYBOARD_ADDRESS_BUTTON_EDIT_NAME = "\u270F  Edit";
    public static final String KEYBOARD_ADDRESS_BUTTON_EDIT_COMMAND = "AddressCallbackEdit";
    public static final String KEYBOARD_ADDRESS_BUTTON_CLOSE_NAME = CLOSE;
    public static final String KEYBOARD_ADDRESS_BUTTON_CLOSE_COMMAND = "AddressCallbackClose";
    public static final String QUERY_ADDRESS_QUESTION_1 = "\uD83E\uDDD9  Напишите ваше ФИО";
    public static final String QUERY_ADDRESS_QUESTION_2 = "\uD83E\uDDD9  Из какого вы города?";
    public static final String QUERY_ADDRESS_QUESTION_3 = "\uD83E\uDDD9  Напишите улицу и номер дома";
    public static final String QUERY_ADDRESS_QUESTION_4 = "\uD83E\uDDD9  Напишите ваш индекс";

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
    public static final String KEYBOARD_FAVORITE_BUTTON_OPTION_PREVIOUS_COMMAND = "FavoriteCallbackPreviousOption";
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

    public static final String KEYBOARD_SECTIONS_HEADER = "\uD83E\uDDD9 Добро пожаловать в наш магазин!";
    public static final String KEYBOARD_SECTIONS_OPERATED_CALLBACK = "SectionsCallback";
    public static final String KEYBOARD_SECTIONS_BUTTON_CLOSE_COMMAND = "SectionsCallbackGoBack";
    public static final String KEYBOARD_SECTIONS_BUTTON_CLOSE_NAME = CLOSE;

    public static final String BOT_START = "Добро пожаловать в наш магазин! " +
            "Здесь вы найдете множество миниатюр для ваших настольных, ролевых игр и варгеймов. " +
            "Если у вас есть вопросы, вы всегда можете задать их в нашем сообществе в " +
            "ВК https://vk.com/goodstuffyouknow";
    public static final String BOT_START_COMMAND = "/start";
    public static final String BOT_ABOUT = "Официальный магазин нашей группы в ВК: " +
            "https://vk.com/goodstuffyouknow. Если вы хотите сообщить о баге или у вас есть предложение " +
            "по улучшению, пожалуйста напишите в этой теме: https://bit.ly/3DRZQlS";
    public static final String BOT_ADDRESS_REQUEST = "Пожалуйста заполните адрес доставки";
    public static final String BOT_ORDER_SUCCESSFUL = "Заказ создан! После того, как заказ будет подтвержден, " +
            "оплатите 100% от его стоимости + 300р за отправку почтой. " +
            "\nНомер карты для перевода (Тинькофф): 5536 9139 3047 6144";
    public static final String ERROR_RESTART_MENU = "Unexpected error, please restart menu";
}