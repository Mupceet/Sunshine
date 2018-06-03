package com.mupceet.sunshine;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mupceet.sunshine.utilities.SunshineDateUtils;
import com.mupceet.sunshine.utilities.SunshineWeatherUtils;

import static com.mupceet.sunshine.MainActivity.INDEX_COLUMN_DATE;
import static com.mupceet.sunshine.MainActivity.INDEX_COLUMN_MAX_TEMP;
import static com.mupceet.sunshine.MainActivity.INDEX_COLUMN_MIN_TEMP;
import static com.mupceet.sunshine.MainActivity.INDEX_COLUMN_WEATHER_ID;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    //    private String[] mWeatherData;
    private final Context mContext;
    private ForecastAdapterOnclickHandler mClickHandler;
    private Cursor mCursor;

    public ForecastAdapter(ForecastAdapterOnclickHandler clickHandler, Context context) {
        mClickHandler = clickHandler;
        mContext = context;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, parent, shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastAdapterViewHolder holder, int position) {
//        String weatherForThisDay = mWeatherData[position];
//        holder.mWeatherTextView.setText(weatherForThisDay);
        mCursor.moveToPosition(position);


        long dateMillis = mCursor.getLong(INDEX_COLUMN_DATE);
        String dateString = SunshineDateUtils.getFriendlyDateString(mContext, dateMillis, false);
        holder.dateView.setText(dateString);

        int weatherId = mCursor.getInt(INDEX_COLUMN_WEATHER_ID);
        int weatherImageId = SunshineWeatherUtils.getSmallArtResourceIdForWeatherCondition(weatherId);
        holder.iconView.setImageResource(weatherImageId);
        String description = SunshineWeatherUtils.getStringForWeatherCondition(mContext, weatherId);
        holder.descriptionView.setText(description);
        holder.descriptionView.setContentDescription(mContext.getString(R.string.ally_forecast, description));
        double highInCelsius = mCursor.getDouble(INDEX_COLUMN_MAX_TEMP);
        double lowInCelsius = mCursor.getDouble(INDEX_COLUMN_MIN_TEMP);
//        String highAndLowTemperature =
//                SunshineWeatherUtils.formatHighLows(mContext, highInCelsius, lowInCelsius);
        String highString = SunshineWeatherUtils.formatTemperature(mContext, highInCelsius);
        holder.highTempView.setText(highString);
        holder.highTempView.setContentDescription(mContext.getString(R.string.ally_high_temp, highString));
        String lowString = SunshineWeatherUtils.formatTemperature(mContext, lowInCelsius);
        holder.lowTempView.setText(lowString);
        holder.lowTempView.setContentDescription(mContext.getString(R.string.ally_low_temp, lowString));

//        String weatherSummary = dateString + " - " + description + " - " + highAndLowTemperature;
//
//        holder.mWeatherTextView.setText(weatherSummary);

    }

    @Override
    public int getItemCount() {
//        if (null == mWeatherData) {
//            return 0;
//        }
//        return mWeatherData.length;
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

//    public void setWeatherData(String[] weatherData) {
//        mWeatherData = weatherData;
//        notifyDataSetChanged();
//    }

    public interface ForecastAdapterOnclickHandler {
        void onForecastAdapterItemClick(long date);
    }

    public class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

//        public final TextView mWeatherTextView;
        final TextView dateView;
        final TextView descriptionView;
        final TextView highTempView;
        final TextView lowTempView;
        final ImageView iconView;


        public ForecastAdapterViewHolder(View itemView) {
            super(itemView);
//            mWeatherTextView = itemView.findViewById(R.id.tv_weather_data);
            dateView = itemView.findViewById(R.id.tv_date);
            descriptionView = itemView.findViewById(R.id.weather_description);
            highTempView = itemView.findViewById(R.id.high_temperature);
            lowTempView = itemView.findViewById(R.id.low_temperature);
            iconView = itemView.findViewById(R.id.weather_icon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
//            String weatherForDay = mWeatherData[adapterPosition];
//            String weatherForDay = mWeatherTextView.getText().toString();
            mCursor.moveToPosition(adapterPosition);
            long dateInMillis = mCursor.getLong(INDEX_COLUMN_DATE);
            mClickHandler.onForecastAdapterItemClick(dateInMillis);
        }
    }
}
