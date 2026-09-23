package org.example.service;

import com.egyptmeters.MeterConnection;
import com.egyptmeters.MeterReader;
import org.example.model.MeterField;

import java.util.List;

public class MeterService {
    MeterReader reader = new MeterReader();
    MeterConnection connection;

    // Connect to the meter
    public boolean connect() {
        try {
            connection = reader.connectMeter("/dev/cu.PL2303G-USBtoUART140");
            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Disconnect from the meter
    public void disconnect() {
        reader.disconnectMeter(connection);
    }

    // Read one meter field
    public String read(MeterField field) {
        Object value;
        try {
            value = reader.readMeterData(
                    connection.getPort(),
                    connection.getClient(),
                    field.getId(),
                    field.getDataType()
            );
        } catch (Exception e) {
            value = "Something went wrong !!";
        }


        return value.toString();
    }

    // Read multiple selected fields.
    public void readFields(List<MeterField> fields) {

        for (MeterField field : fields) {

            String value = read(field);

            field.setValue(value);
        }
    }
}