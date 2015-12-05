package barqsoft.footballscores;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities
{
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;
    public static int getLeague(int league_num)
    {
        switch (league_num)
        {
            case SERIE_A : return R.string.series_a_text;
            case PREMIER_LEGAUE : return R.string.premierLeague_text ;
            case CHAMPIONS_LEAGUE : return R.string.uefa_text;
            case PRIMERA_DIVISION : return R.string.primera_text;
            case BUNDESLIGA : return R.string.bundesliga_text;
            default: return R.string.not_known_league_text;
        }
    }
    public static String getMatchDay(int match_day,int league_num, Context context)
    {
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return context.getString(R.string.group_stage_match_day_6_text);
            }
            else if(match_day == 7 || match_day == 8)
            {
                return context.getString( R.string.first_knockout_round_text);
            }
            else if(match_day == 9 || match_day == 10)
            {
                return context.getString(R.string.quaterfinal_text) ;
            }
            else if(match_day == 11 || match_day == 12)
            {
                return context.getString(R.string.semifinal_day_text) ;
            }
            else
            {
                return context.getString(R.string.final_day_text);
            }
        }
        else
        {
            return context.getString(R.string.match_day_text ) + " : " + String.valueOf(match_day);
        }
    }


    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            default: return R.drawable.no_icon;
        }
    }

    public static String getServerStatus(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return  sharedPreferences.getString("server-status", context.getString(R.string.no_matches));
    }
}
