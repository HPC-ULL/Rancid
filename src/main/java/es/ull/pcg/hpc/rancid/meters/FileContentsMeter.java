package es.ull.pcg.hpc.rancid.meters;

import es.ull.pcg.hpc.rancid.Meter;
import es.ull.pcg.hpc.rancid.results.ResultTypes;
import es.ull.pcg.hpc.rancid.results.ValueResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Scanner;

/**
 * Meter that obtains its measurements from reading a single number from a file. If there is a problem reading the file,
 * it stores the result of {@link #errorValue()} instead.
 */
public class FileContentsMeter implements Meter {
    private final String mFileName, mMeterName;
    private Number mValue;

    public FileContentsMeter (String fileName, String meterName) {
        this.mFileName = fileName;
        this.mMeterName = meterName;
    }

    @Override
    public void start () {
        mValue = -1;
    }

    @Override
    public void stop () {
        mValue = readFileValue(mFileName);
    }

    @Override
    public void stopError () {}

    @Override
    public void reset () {}

    @Override
    public String getTitle () {
        return mMeterName;
    }

    @Override
    public ValueResult getMeasure () {
        return new ValueResult(getTitle(), ResultTypes.Value, mValue);
    }

    protected Number errorValue () {
        return Float.NaN;
    }

    private Number readFileValue (String fileName) {
        try {
            String str;

            File file = new File(fileName);
            try (Scanner s = new Scanner(file).useDelimiter("\\Z")) {
                str = s.next();
            }

            return NumberFormat.getInstance().parse(str);
        } catch (FileNotFoundException | NumberFormatException | ParseException e) {
            return errorValue();
        }
    }
}
