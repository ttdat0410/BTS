package vn.vnpt.ansv.bts.ui.monitor;

import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vn.vnpt.ansv.bts.R;
import vn.vnpt.ansv.bts.objects.MinSensorFullObj;
import vn.vnpt.ansv.bts.utils.Utils;

/**
 * Created by ANSV on 11/9/2017.
 */

public class RecyclerMonitorAdapter extends RecyclerView.Adapter<RecyclerMonitorAdapter.DeviceHolder> {

    private List<MinSensorFullObj> dataSet;
    private OnDeviceItemClickListener listener;

    public RecyclerMonitorAdapter(List<MinSensorFullObj> dataSet) {
        this.dataSet = dataSet;
    }

    @Override
    public int getItemCount() {
        if (dataSet == null) {
            return 0;
        } else {
            return dataSet.size();
        }
    }

    @Override
    public DeviceHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_device, parent, false);
        return new DeviceHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceHolder holder, int position) {

        FrameLayout.LayoutParams lp =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        holder.cardView.setLayoutParams(lp);
        holder.cardView.requestLayout();
        //
        String value = "";
        String measurement = dataSet.get(position).getSensorInfo().getMeasurementUnit();
        int batteryValue = dataSet.get(position).getSensorData().getList().get(0).getBattery();
        int stationId = dataSet.get(position).getSensorData().getList().get(0).getStatusId();

        holder.txtSensorName.setText(dataSet.get(position).getSensorInfo().getSensorName().toUpperCase());
        holder.txtSensorValue.setText(dataSet.get(position).getSensorData().getList().get(0).getValue()+"");
        holder.txtSensorValue.setTextColor(ContextCompat.getColor(holder.rootView.getContext(), Utils.setColorForSensorValue(stationId)));
        holder.txtBatteryValues.setText(batteryValue + "%");
        holder.txtBatteryValues.setTextColor(ContextCompat.getColor(holder.rootView.getContext(), Utils.setColorForBatteryValue(batteryValue)));
        holder.imgBatteryIcon.setImageResource(Utils.setBatteryImageView(batteryValue));
        if (measurement == null) {
            value = dataSet.get(position).getSensorData().getList().get(0).getValue()+"";
            holder.txtMeasurementUnit.setText("");

        } else {
            value = dataSet.get(position).getSensorData().getList().get(0).getValue()+"";
            holder.txtMeasurementUnit.setText("("+measurement+")");
        }

        /*for (int z = 0; z < listSensorObj.size(); z++) {
                                        int sensorId = listSensorObj.get(z).getSensorInfo().getSensorId();
                                        String sensorName = listSensorObj.get(z).getSensorInfo().getSensorName();
                                        String sensorSerial = listSensorObj.get(z).getSensorInfo().getSensorSerial();
                                        int sensorTypeId = listSensorObj.get(z).getSensorInfo().getSensorTypeId();
                                        String measurementUnit = listSensorObj.get(z).getSensorInfo().getMeasurementUnit();
                                        int warningModeId = listSensorObj.get(z).getSensorInfo().getWarningModeId();
                                        int warningValue1 = listSensorObj.get(z).getSensorInfo().getWarningValue1();
                                        int warningValue2 = listSensorObj.get(z).getSensorInfo().getWarningValue2();
                                        int warningComp = listSensorObj.get(z).getSensorInfo().getWarningComp();

                                        SensorInfoObj sensorInfoObj = new SensorInfoObj(sensorId,
                                                sensorName, sensorSerial, sensorTypeId,
                                                measurementUnit, warningModeId,
                                                warningValue1, warningValue2, warningComp);

                                        List<SensorDataObj> sensorData = listSensorObj.get(z).getSensorData().getList();
                                        String sensorValue = String.valueOf(sensorData.get(0).getValue());
                                        Log.i("0x00", sensorInfoObj.getSensorName() + " " + sensorInfoObj.getMeasurementUnit() + " | " + sensorValue + "\n");
                                    }*/


//        holder.rootView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (listener != null) {
//                    listener.onDeviceItemClick(v, device);
//                }
//            }
//        });
    }


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void updateDataSet(List<MinSensorFullObj> listSensorObj) {
        this.dataSet = null;
        this.dataSet = listSensorObj;
        notifyDataSetChanged();
        Log.i("0x00", this.dataSet.size() + " SIZE");
    }

    public void setListener(OnDeviceItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnDeviceItemClickListener {
        void onDeviceItemClick(View view, Object device);
    }

    public static class DeviceHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.card_view)
        CardView cardView;

        @BindView(R.id.txtSensorName)
        TextView txtSensorName;

        @BindView(R.id.txtSensorValue)
        TextView txtSensorValue;

        @BindView(R.id.txtMeasurementUnit)
        TextView txtMeasurementUnit;

        @BindView(R.id.txtBatteryValues)
        TextView txtBatteryValues;

        @BindView(R.id.imgSensorIcon)
        ImageView imgSensorIcon;

        @BindView(R.id.imgBatteryIcon)
        ImageView imgBatteryIcon;

        View rootView;

        public DeviceHolder(View view) {
            super(view);
            rootView = view;
            ButterKnife.bind(this, view);
        }
    }
}
