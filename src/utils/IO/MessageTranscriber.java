/**
 * Provides methods to encode/decode messages for client/server IO
 * Created by Max Buster
 */

package utils.IO;

import model.Candidate.CandidateInfo;
import model.Votes.VoteResults;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageTranscriber {

    // ------------------------------- Info Request ----------------------------------- //

    public static Object encode_info_request(int candidate_num) {
        HashMap<String, Integer> encoded_info = new HashMap<String, Integer>();
        encoded_info.put("Candidate Num", candidate_num);
        return encoded_info;
    }

    public static HashMap<String, Integer> decode_info_request(Object encoded_info) {
        HashMap<String, Integer> decoded_info = (HashMap<String, Integer>) encoded_info;
        return decoded_info;
    }

    // ------------------------------- End Round ----------------------------------- //

    public static Object encode_end_round() {
        return null;
    }

    public static Object decode_end_round(Object encoded_info) {
        return null;
    }

    // ------------------------------- Client Vote ----------------------------------- //

    public static Object encode_vote(int candidate_num) {
        HashMap<String, Integer> encoded_info = new HashMap<String, Integer>();
        encoded_info.put("Candidate Num", candidate_num);
        return encoded_info;
    }

    public static HashMap<String, Integer> decode_vote(Object encoded_info) {
        HashMap<String, Integer> decoded_info = (HashMap<String, Integer>) encoded_info;
        return decoded_info;
    }


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

    // ------------------------------- Game Info ----------------------------------- //

    public static Object encode_game_info(int game_num, int budget, int max_bias) {
        HashMap<String, Integer> encoded_info = new HashMap<String, Integer>();
        encoded_info.put("Game Num", game_num);
        encoded_info.put("Budget", budget);
        encoded_info.put("Max Bias", max_bias);
        return encoded_info;
    }

    public static HashMap<String, Integer> decode_game_info(Object encoded_info) {
        HashMap<String, Integer> decoded_info = (HashMap<String, Integer>) encoded_info;
        return decoded_info;
    }

    // ------------------------------- Voter Info ----------------------------------- //

    public static Object encode_voter_info(int std_dev_1, int mean_1, int std_dev_2, int mean_2) {
        HashMap<String, Integer> encoded_info = new HashMap<String, Integer>();
        encoded_info.put("Std Dev 1", std_dev_1);
        encoded_info.put("Mean 1", mean_1);
        encoded_info.put("Std Dev 2", std_dev_2);
        encoded_info.put("Mean 2", mean_2);
        return encoded_info;
    }

    public static HashMap<String, Integer> decode_voter_info(Object encoded_info) {
        HashMap<String, Integer> decoded_info = (HashMap<String, Integer>) encoded_info;
        return decoded_info;
    }

    // ------------------------------- Candidate Info ----------------------------------- //

    public static Object encode_candidate_info(ArrayList<CandidateInfo> candidate_info) {
        return candidate_info;
    }

    public static ArrayList<CandidateInfo> decode_candidate_info(Object encoded_info) {
        ArrayList<CandidateInfo> decoded_info = (ArrayList<CandidateInfo>) encoded_info;
        return decoded_info;
    }

    // ------------------------------- Round Info ----------------------------------- //

    public static Object encode_round_info(int round_num) {
        HashMap<String, Integer> encoded_info = new HashMap<String, Integer>();
        encoded_info.put("Round Num", round_num);
        return encoded_info;
    }

    public static HashMap<String, Integer> decode_round_info(Object encoded_info) {
        HashMap<String, Integer> decoded_info = (HashMap<String, Integer>) encoded_info;
        return decoded_info;
    }

    // ------------------------------- Vote Results ----------------------------------- //

    public static Object encode_vote_results(HashMap<Integer, VoteResults> vote_results) {
        return vote_results;
    }

    public static HashMap<Integer, VoteResults> decode_vote_results(Object encoded_info) {
        HashMap<Integer, VoteResults> decoded_info = (HashMap<Integer, VoteResults>) encoded_info;
        return decoded_info;
    }

    // ------------------------------- End of Game ----------------------------------- //

    public static Object encode_end_of_game(int winning_candidate, int round_winnings, int game_winnings, int current_game) {
        HashMap<String, Integer> encoded_info = new HashMap<String, Integer>();
        encoded_info.put("Winning Candidate", winning_candidate);
        encoded_info.put("Round Winnings", round_winnings);
        encoded_info.put("Game Winnings", game_winnings);
        encoded_info.put("Current Game Num", current_game);
        return encoded_info;
    }

    public static HashMap<String, Integer> decode_end_of_game(Object encoded_info) {
        HashMap<String, Integer> decoded_info = (HashMap<String, Integer>) encoded_info;
        return decoded_info;
    }

    // ------------------------------- Terminate Game ----------------------------------- //

    public static Object encode_terminate_game() {
        return null;
    }

    public static Object decode_terminate_game(Object encoded_info) {
        return null;
    }
}
