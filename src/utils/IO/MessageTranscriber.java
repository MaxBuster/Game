/**
 * Provides methods to encode/decode messages for client/server IO
 * Created by Max Buster
 */

package utils.IO;

import java.util.HashMap;

public class MessageTranscriber {

    // ------------------------------- Initial Info ----------------------------------- //

    public static Object encode_initial_info(int player_num, int num_games) {
        HashMap<String, Integer> encoded_info = new HashMap<String, Integer>();
        encoded_info.put("Player Number", player_num);
        encoded_info.put("Num Games", num_games);
        return encoded_info;
    }

    public static HashMap<String, Integer> decode_initial_info(Object encoded_info) {
        HashMap<String, Integer> decoded_info = (HashMap<String, Integer>) encoded_info;
        return decoded_info;
    }

    // ------------------------------- Player Info ----------------------------------- //

    public static Object encode_player_info(int player_position) {
        HashMap<String, Integer> encoded_info = new HashMap<String, Integer>();
        encoded_info.put("Player Position", player_position);
        return encoded_info;
    }

    public static HashMap<String, Integer> decode_player_info(Object encoded_info) {
        HashMap<String, Integer> decoded_info = (HashMap<String, Integer>) encoded_info;
        return decoded_info;
    }
}
