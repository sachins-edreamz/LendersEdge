package com.edz.android.lendersedge.Fragment;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.edz.android.lendersedge.R;
import com.edz.android.lendersedge.Utils.Utils;
import com.edz.android.lendersedge.Web.WebHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class FirstCalsiFragment extends Fragment implements OnClickListener {

    public static final String ARG_PLANET_NUMBER = "number";
    private View view;
    private TextView mTxtDate, mTxtFirstType;
    private Spinner mSpinRateIndex, mSpinPayFreq, mSpinIntDyCnt;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioCreditMargin, mRadioFixedRate;
    private Button btnReset, btnEmail, btnCalculate;
    private Utils mUtils;
    private WebHandler mWebHandler;

    private EditText mEdtLoanAmount, mEdtFixedRateTeram, mEdtLoanAmortization,
            mEdtFixedCredit, mEdtIndexRate, mEdtAllIndexRate, mEdtFinalPayment,
            mEdtMonthlyPayment, mEdtTotalInterst, mEdtLoanAmortizaitonMonth;
    private double dblMonths = 0;
    private double ratevaliValue = 0;
    private double fdmwaliValue = 0;
    private double dblAmort = 0;
    private TextView MonthlyPaymentTitle;
    private String strCurrentDate, strNewDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater
                .inflate(R.layout.first_calsi_fragment, container, false);
        mTxtDate = (TextView) view.findViewById(R.id.txtDate);
        mTxtFirstType = (TextView) view.findViewById(R.id.type_calsi);
        mSpinRateIndex = (Spinner) view.findViewById(R.id.spinRateIndex);
        mSpinPayFreq = (Spinner) view.findViewById(R.id.spinPaymentFrequncy);
        mSpinIntDyCnt = (Spinner) view.findViewById(R.id.spinInterstDayCount);
        mRadioGroup = (RadioGroup) view.findViewById(R.id.radioGrup);
        mEdtIndexRate = (EditText) view.findViewById(R.id.edtIndexRate);
        mEdtAllIndexRate = (EditText) view.findViewById(R.id.AllInIndexRAte);
        mEdtFinalPayment = (EditText) view.findViewById(R.id.mEdtFinalPayment);
        mEdtMonthlyPayment = (EditText) view
                .findViewById(R.id.mEdtMonthlyPayment);
        mEdtTotalInterst = (EditText) view.findViewById(R.id.mEdtTotalInterest);
        mRadioCreditMargin = (RadioButton) view
                .findViewById(R.id.radioCreditMargin);
        mRadioFixedRate = (RadioButton) view.findViewById(R.id.radioFixedRate);
        btnReset = (Button) view.findViewById(R.id.btnReset);
        btnEmail = (Button) view.findViewById(R.id.btnEmail);
        btnCalculate = (Button) view.findViewById(R.id.btnCalculate);
        mEdtLoanAmount = (EditText) view.findViewById(R.id.mEdtLoanAmount);
        MonthlyPaymentTitle = (TextView) view
                .findViewById(R.id.titleMontlyQrtySemi);
        mEdtLoanAmount.addTextChangedListener(new TextWatcher() {

            boolean isManualChange = false;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (isManualChange) {
                    isManualChange = false;
                    return;
                }

                try {
                    String value = s.toString().replace(",", "");
                    String reverseValue = new StringBuilder(value).reverse()
                            .toString();
                    StringBuilder finalValue = new StringBuilder();
                    for (int i = 1; i <= reverseValue.length(); i++) {
                        char val = reverseValue.charAt(i - 1);
                        finalValue.append(val);
                        if (i % 3 == 0 && i != reverseValue.length() && i > 0) {
                            finalValue.append(",");
                        }
                    }
                    isManualChange = true;
                    mEdtLoanAmount.setText(finalValue.reverse());
                    mEdtLoanAmount.setSelection(finalValue.length());
                } catch (Exception e) {
                    // Do nothing since not a number
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        mEdtLoanAmortization = (EditText) view
                .findViewById(R.id.mEdtLoanAmortization);
        mEdtLoanAmortizaitonMonth = (EditText) view
                .findViewById(R.id.mEdtLoanAmortizationMonth);
        mEdtFixedRateTeram = (EditText) view.findViewById(R.id.mEdtTerm);
        mEdtFixedCredit = (EditText) view.findViewById(R.id.mEdtFixedCredit);

        btnReset.setOnClickListener(this);
        btnEmail.setOnClickListener(this);
        btnCalculate.setOnClickListener(this);
        mUtils = new Utils(getActivity());

        mWebHandler = new WebHandler();

        mTxtDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datePicker");
            }
        });
        addSpinRateIndex();
        addSpinPayFreq();
        addSpinInterestDayCount();
        setCurrentDate();

        mRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // TODO Auto-generated method stub
                if (checkedId == R.id.radioCreditMargin) {
                    mRadioCreditMargin.setTextColor(Color.BLACK);
                    mRadioFixedRate.setTextColor(Color.WHITE);
                    mTxtFirstType.setText("Credit Margin");
                } else if (checkedId == R.id.radioFixedRate) {
                    mRadioCreditMargin.setTextColor(Color.WHITE);
                    mRadioFixedRate.setTextColor(Color.BLACK);
                    mTxtFirstType.setText("All In Fixed Rate");
                }
            }
        });
        return view;
    }

    private void setCurrentDate() {
        // TODO Auto-generated method stub
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        strCurrentDate = ""
                + getDate("" + (1 + month) + "/" + day + "/" + year);
        strNewDate = strCurrentDate;
        mTxtDate.setText(strCurrentDate);
    }

    public class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user

            strNewDate = ""
                    + getDate("" + (1 + month) + "/" + day + "/" + year);
            mTxtDate.setText(strNewDate);

        }
    }

    private void addSpinRateIndex() {
        // TODO Auto-generated method stub
       /* ArrayAdapter<String> categoryAdapter = new ArrayAdapter<String>(
                getActivity(), R.drawable.new_spin_items, Utils.strRateIndex);
        categoryAdapter.setDropDownViewResource(R.drawable.popup_spin_items);
        mSpinRateIndex.setAdapter(categoryAdapter);*/
    }

    private void addSpinPayFreq() {
        // TODO Auto-generated method stub

       /* ArrayAdapter<String> mPaymentFeq = new ArrayAdapter<String>(
                getActivity(), R.drawable.new_spin_items, Utils.strPayFreq);
        mPaymentFeq.setDropDownViewResource(R.drawable.popup_spin_items);
        mSpinPayFreq.setAdapter(mPaymentFeq);*/

    }

    private CharSequence getDate(String strDate) {
        // TODO Auto-generated method stub
        String str = strDate;
        SimpleDateFormat mFormatOriginal = new SimpleDateFormat("MM/dd/yyyy");
        SimpleDateFormat mFormatNew = new SimpleDateFormat("MMM dd, yyyy");

        try {
            Date date = mFormatOriginal.parse(strDate);

            str = mFormatNew.format(date);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return str;
    }

    private void addSpinInterestDayCount() {
        // TODO Auto-generated method stub
    /*    ArrayAdapter<String> mAdapterInterestDayCount = new ArrayAdapter<String>(
                getActivity(), R.drawable.new_spin_items, Utils.strIntDayCount);
        mAdapterInterestDayCount
                .setDropDownViewResource(R.drawable.popup_spin_items);
        mSpinIntDyCnt.setAdapter(mAdapterInterestDayCount);*/
    }

    /*
     * Following method is used onClick for all button
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.getId() == R.id.btnReset) {

            mEdtAllIndexRate.setText("");
            mEdtFixedCredit.setText("");
            mEdtFixedRateTeram.setText("");
            mEdtIndexRate.setText("");
            mEdtLoanAmortization.setText("");
            mEdtLoanAmount.setText("");
            mEdtMonthlyPayment.setText("");
            mEdtTotalInterst.setText("");
            mEdtFinalPayment.setText("");

            // refreshallvariable();
        } else if (v.getId() == R.id.btnEmail) {

        } else if (v.getId() == R.id.btnCalculate) {

            if (mUtils.isInterentConnection()) {

                if (isValidate()) {

                    String fixedRate = getFixedRate(mEdtFixedRateTeram
                            .getText().toString().trim());

                    String LoanAmort = "";
                    if (isEmpty(mEdtLoanAmortizaitonMonth)) {
                        LoanAmort = getLoanAmortization(mEdtLoanAmortization
                                .getText().toString().trim());
                    } else {
                        LoanAmort = getLoanAmortization(mEdtLoanAmortization
                                .getText().toString().trim()
                                + " "
                                + mEdtLoanAmortizaitonMonth.getText()
                                .toString().trim());
                    }

                    String res = Utils.strRateIndexIds[mSpinRateIndex
                            .getSelectedItemPosition()]
                            + ""

                            + Utils.strIndDayCountId[mSpinIntDyCnt
                            .getSelectedItemPosition()]
                            + ""
                            + Utils.strPayFreqIds[mSpinPayFreq
                            .getSelectedItemPosition()];

                    new GetRateIndex().execute(fixedRate.trim(),
                            LoanAmort.trim(), res.trim());

                }

            } else {
                mUtils.mToast(Utils.INTERNET_CONNECTION);
            }
        }

    }

    /**
     * Following
     *
     * @param str
     * @return
     */
    private String getFixedRate(String str) {
        // TODO Auto-generated method stub

        String term = "";

        String search = "yrs";
        if (str.toLowerCase().indexOf(search.toLowerCase()) != -1) {

            term = str.replaceAll("[^0-9.]", "");
        } else {
            term = str;
        }

        return term;

    }

    /**
     * Following method is used to check all inputs are empty or not
     *
     * @return
     */
    private boolean isValidate() {
        // TODO Auto-generated method stub

        if (isEmpty(mEdtLoanAmount)) {
            mEdtLoanAmount.requestFocus();
            mUtils.mToast("enter loan amount");
            return false;
        } else if (isEmpty(mEdtFixedRateTeram)) {
            mEdtFixedRateTeram.requestFocus();
            mUtils.mToast("enter fixed rate term");
            return false;
        } else if (isEmpty(mEdtLoanAmortization)) {
            mEdtLoanAmortization.requestFocus();
            mUtils.mToast("enter loan amortization");
            return false;
        } else if (isEmpty(mEdtFixedCredit)) {
            mEdtFixedCredit.requestFocus();
            mUtils.mToast("enter fixed rate value");
            return false;
        }

        return true;
    }

    private boolean isEmpty(EditText mEdt) {
        // TODO Auto-generated method stub
        if (mEdt.getText().toString().trim().length() == 0) {
            return true;
        }

        return false;
    }

    /**
     * Following method is used to get loan amortization value
     *
     * @param str
     * @return
     */
    private String getLoanAmortization(String str) {
        // TODO Auto-generated method stub

        String[] splited = str.split("\\s+");
        String term = "";

        if (splited.length == 2) {

            term = splited[0];
            double month = Double.parseDouble(splited[1]);
            dblAmort = Double.parseDouble(term);
            if (month % 3 == 0) {

                if (month / 3 == 1) {
                    term = term + ".25";
                } else if (month / 3 == 2) {
                    term = term + ".50";
                } else if (month / 3 == 3) {
                    term = term + ".75";
                } else if (month / 3 == 4) {
                    term = term + ".00";
                }
                dblMonths = 0;

            } else if (month % 3 == 1) {

                dblMonths = 1.0;

                int i = (int) (month / 3);
                double tmpMonth = i * 3;

                if (tmpMonth / 3 == 1) {
                    term = term + ".25";
                } else if (tmpMonth / 3 == 2) {
                    term = term + ".50";
                } else if (tmpMonth / 3 == 3) {
                    term = term + ".75";
                } else if (tmpMonth / 3 == 4) {
                    term = term + ".00";
                }

            } else if (month % 3 == 2) {

                dblMonths = 2.0;

                int i = (int) (month / 3);
                double tmpMonth = i * 3;

                if (tmpMonth / 3 == 1) {
                    term = term + ".25";
                } else if (tmpMonth / 3 == 2) {
                    term = term + ".50";
                } else if (tmpMonth / 3 == 3) {
                    term = term + ".75";
                } else if (tmpMonth / 3 == 4) {
                    term = term + ".00";
                }
            }

        } else if (splited.length == 1) {

            term = splited[0] + ".00";
            dblAmort = Double.parseDouble(term);
        }

        return term;

    }

    private class GetRateIndex extends AsyncTask<String, String, String> {
        private ProgressDialog mDlg;
        private String strResp = "";
        private Exception myException;
        private JSONObject mObject;
        private JSONArray mArray;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            mDlg = new ProgressDialog(getActivity());
            mDlg.setMessage("Please wait...");
            mDlg.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                strResp = mWebHandler.getRateIndex(params[0], params[1],
                        params[2]);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                myException = e;
            }

            return strResp;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            mDlg.dismiss();
            if (myException == null) {
                try {
                    mObject = new JSONObject(result);
                    if (mObject.getString("Message").equals("Success")) {

                        mArray = mObject.getJSONArray("Response");

                        for (int i = 0; i < mArray.length(); i++) {

                            ratevaliValue = Double.parseDouble(mArray
                                    .getJSONObject(i).getString("value"));
                            fdmwaliValue = Double.parseDouble(mArray
                                    .getJSONObject(i).getString("fwdprm"));

                        }

                        Double dblDay = Double
                                .parseDouble(getCurrentDateDiffernce(
                                        strCurrentDate, strNewDate));

                        Log.e("dblDay", "" + dblDay);
                        double rateIndexTotal = 0.0;
                        if (dblMonths > 0) {
                            double val = (0.083 * ratevaliValue);

                            double val2 = val / dblAmort;

                            double mainval = 0.0;

                            if (dblMonths == 1) {

                                mainval = ratevaliValue + (val * 1);

                            } else if (dblMonths == 2) {

                                mainval = ratevaliValue + (val * 2);
                            } else {
                                mainval = ratevaliValue;
                            }

                            rateIndexTotal = mainval
                                    + ((dblDay / 30) * fdmwaliValue);

                            Log.e("RateIndex", "" + rateIndexTotal);
                            refreshallvariable();
                            mEdtIndexRate.setText(""
                                    + String.format("%.2f", rateIndexTotal));
                        } else {

                            rateIndexTotal = ratevaliValue
                                    + ((dblDay / 30) * fdmwaliValue);
                            Log.e("RateIndex", "" + rateIndexTotal);
                            refreshallvariable();
                            mEdtIndexRate.setText(""
                                    + String.format("%.2f", rateIndexTotal));
                        }

                        // if Fixed rate is enable

                        if (mRadioFixedRate.isChecked()) {

                            double dblmedtFiexedCrdit = Double
                                    .parseDouble(mEdtFixedCredit.getText()
                                            .toString().trim()
                                            .replaceAll("[^0-9.]", ""));
                            double dblmedtRateIndex = Double
                                    .parseDouble(mEdtIndexRate.getText()
                                            .toString().trim()
                                            .replaceAll("[^0-9.]", ""));

                            String total = String.format("%.2f",
                                    (dblmedtFiexedCrdit + dblmedtRateIndex));

                            mEdtAllIndexRate.setText(total);

                        }

                        // if credit margin is enable
                        else if (mRadioCreditMargin.isChecked()) {
                            double dblmedtFiexedCrdit = Double
                                    .parseDouble(mEdtFixedCredit.getText()
                                            .toString().trim()
                                            .replaceAll("[^0-9.]", ""));
                            double dblmedtRateIndex = Double
                                    .parseDouble(mEdtIndexRate.getText()
                                            .toString().trim()
                                            .replaceAll("[^0-9.]", ""));

                            String total = String.format("%.2f",
                                    (dblmedtFiexedCrdit - dblmedtRateIndex));

                            mEdtAllIndexRate.setText(total);
                        }

                        // finalpayment();
                        // SecondStepPaymentMethod();

                        calculateMonthlyPayment();

                    } else {
                        mUtils.mToast("Something wrong please try again..");

                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                mUtils.mToast("Something wrong please try again..");
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private String getCurrentDateDiffernce(String strDate1, String strDate2) {

        String strDays = "0";

        SimpleDateFormat myFormat = new SimpleDateFormat("MMM dd, yyyy");
        String inputString1 = strDate1;
        String inputString2 = strDate2;

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            long diff = date2.getTime() - date1.getTime();
            strDays = "" + (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("Old Date", strDate1);
        Log.e("New Date", strDate2);
        Log.e("diffrence day", "" + strDays);
        return strDays;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private String getCurrentDateDiffernceForDateWithNewFormat(String strDate1,
                                                               String strDate2) {

        String strDays = "0";
        SimpleDateFormat myFormat = new SimpleDateFormat("dd/MM/yyyy");
        String inputString1 = strDate1;
        String inputString2 = strDate2;

        try {
            Date date1 = myFormat.parse(inputString1);
            Date date2 = myFormat.parse(inputString2);
            long diff = date2.getTime() - date1.getTime();
            strDays = "" + (TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            // Log.i("IN FUNCIOM DIFF", strDate1 + "-" + strDate2 + " =" +
            // strDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return strDays;
    }

    private void refreshallvariable() {
        // TODO Auto-generated method stub

        dblMonths = 0;
        ratevaliValue = 0;
        fdmwaliValue = 0;
        dblAmort = 0;
        setCurrentDate();

    }

    private void calculateMonthlyPayment() {
        // TODO Auto-generated method stub

        if (mSpinIntDyCnt.getSelectedItemPosition() == 0) {

            double intRate = mUtils.convertStringToDouble(mEdtAllIndexRate
                    .getText().toString().trim());

            double loanAmount = mUtils
                    .convertStringToDouble(getNumbers(mEdtLoanAmount.getText()
                            .toString().trim()));

            int numberOfMonth = convertLoanAmortization(mEdtLoanAmortization
                    .getText().toString().trim(), mEdtLoanAmortizaitonMonth
                    .getText().toString().trim());

            // double mintRate = (intRate / 12) / 100;
            double finalIntrate = (double) getCurrentYearDays() * intRate / 360;

            double mnthPyament = 0;
            mnthPyament = calculatMonthlyPayment(loanAmount, finalIntrate,
                    numberOfMonth);

            mEdtMonthlyPayment.setText("" + mnthPyament);

            calculateTotalPayment(loanAmount, mnthPyament, numberOfMonth);

        } else if (mSpinIntDyCnt.getSelectedItemPosition() == 1) {
            double intRate = mUtils.convertStringToDouble(mEdtAllIndexRate
                    .getText().toString().trim());

            double loanAmount = mUtils
                    .convertStringToDouble(getNumbers(mEdtLoanAmount.getText()
                            .toString().trim()));

            int numberOfMonth = convertLoanAmortization(mEdtLoanAmortization
                    .getText().toString().trim(), mEdtLoanAmortizaitonMonth
                    .getText().toString().trim());

            // double mintRate = (intRate / 12) / 100;
            double finalIntrate = (double) getCurrentYearDays() * intRate / 365;

            double mnthPyament = 0;
            mnthPyament = calculatMonthlyPayment(loanAmount, finalIntrate,
                    numberOfMonth);

            mEdtMonthlyPayment.setText("" + mnthPyament);

            calculateTotalPayment(loanAmount, mnthPyament, numberOfMonth);

        } else if (mSpinIntDyCnt.getSelectedItemPosition() == 2) {
            double intRate = mUtils.convertStringToDouble(mEdtAllIndexRate
                    .getText().toString().trim());

            double loanAmount = mUtils
                    .convertStringToDouble(getNumbers(mEdtLoanAmount.getText()
                            .toString().trim()));
            int numberOfMonth = convertLoanAmortization(mEdtLoanAmortization
                    .getText().toString().trim(), mEdtLoanAmortizaitonMonth
                    .getText().toString().trim());

            // double mintRate = (intRate / 12) / 100;
            double finalIntrate = (double) getCurrentYearDays() * intRate
                    / (double) getCurrentYearDays();

            double mnthPyament = 0;
            mnthPyament = calculatMonthlyPayment(loanAmount, finalIntrate,
                    numberOfMonth);

            mEdtMonthlyPayment.setText("" + mnthPyament);

            calculateTotalPayment(loanAmount, mnthPyament, numberOfMonth);

        } else if (mSpinIntDyCnt.getSelectedItemPosition() == 3) {
            double intRate = mUtils.convertStringToDouble(mEdtAllIndexRate
                    .getText().toString().trim());

            double loanAmount = mUtils
                    .convertStringToDouble(getNumbers(mEdtLoanAmount.getText()
                            .toString().trim()));

            int numberOfMonth = convertLoanAmortization(mEdtLoanAmortization
                    .getText().toString().trim(), mEdtLoanAmortizaitonMonth
                    .getText().toString().trim());

            // double mintRate = (intRate / 12) / 100;
            double finalIntrate = 365 * intRate / 360;

            double mnthPyament = 0;
            mnthPyament = calculatMonthlyPayment(loanAmount, finalIntrate,
                    numberOfMonth);

            mEdtMonthlyPayment.setText("" + mnthPyament);

            calculateTotalPayment(loanAmount, mnthPyament, numberOfMonth);

        } else if (mSpinIntDyCnt.getSelectedItemPosition() == 4) {
            double intRate = mUtils.convertStringToDouble(mEdtAllIndexRate
                    .getText().toString().trim());

            double loanAmount = mUtils
                    .convertStringToDouble(getNumbers(mEdtLoanAmount.getText()
                            .toString().trim()));
            int numberOfMonth = convertLoanAmortization(mEdtLoanAmortization
                    .getText().toString().trim(), mEdtLoanAmortizaitonMonth
                    .getText().toString().trim());
            // double mintRate = (intRate / 12) / 100;


            double finalIntrate = 30 * intRate / 360;


            double mnthPyament = 0;
            mnthPyament = calculatMonthlyPayment(loanAmount, finalIntrate,
                    numberOfMonth);


            mEdtMonthlyPayment.setText("" + mnthPyament);

            calculateTotalPayment(loanAmount, mnthPyament, numberOfMonth);
        }

    }

    private void calculateTotalPayment(double pv, double p, int n2) {
        // TODO Auto-generated method stub

        double n = convertTerm(mEdtFixedRateTeram.getText().toString().trim());

        // n = n - 1;
        double r = 0;
        double fv = 0.0;
        if (mRadioFixedRate.isChecked()) {
            r = mUtils.convertStringToDouble(mEdtAllIndexRate.getText()
                    .toString().trim());
        } else {
            r = mUtils.convertStringToDouble(mEdtFixedCredit.getText()
                    .toString().trim());
        }

        if (mSpinPayFreq.getSelectedItemPosition() == 0) {
            r = (r / 100) / 12;
        } else if (mSpinPayFreq.getSelectedItemPosition() == 1) {
            r = (r / 100) / 4;
        } else if (mSpinPayFreq.getSelectedItemPosition() == 2) {
            r = (r / 100) / 2;
        } else if (mSpinPayFreq.getSelectedItemPosition() == 3) {
            r = (r / 100);
        }

        double j = Math.pow(1 + r, n);

        double jj = (j - 1) / r;

        fv = (pv * j) - (p * jj);

        mEdtFinalPayment.setText(String.format("%.2f", (fv + p)));

        double totalPayment = (p * n);
        double totalInt = 0;
        if (n == n2) {

            totalInt = (totalPayment - pv);

        } else {
            totalInt = (totalPayment - (pv - (fv + p)));

        }

        mEdtTotalInterst.setText(String.format("%.2f", totalInt));

    }

    private String getNumbers(String str) {
        // TODO Auto-generated method stub

        String term = "";

        term = str.replaceAll("[^0-9.]", "");

        return term;

    }

    /**
     * Following method is used to get Current year days
     *
     * @return count
     */
    public int getCurrentYearDays() {

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());

        int numOfDays = cal.getActualMaximum(Calendar.DAY_OF_YEAR);

        return numOfDays;
    }


    /**
     * Following method is used to convert convert loan amortization year into
     * months
     *
     * @param str
     * @param strMonth
     * @return months
     */
    private int convertLoanAmortization(String str, String strMonth) {
        // TODO Auto-generated method stub

        int val = 1;
        if (mSpinPayFreq.getSelectedItemPosition() == 0) {
            val = 12;
            MonthlyPaymentTitle.setText("Monthly Payment");
        } else if (mSpinPayFreq.getSelectedItemPosition() == 1) {
            val = 4;
            MonthlyPaymentTitle.setText("Quarterly Payment");
        } else if (mSpinPayFreq.getSelectedItemPosition() == 2) {
            val = 2;
            MonthlyPaymentTitle.setText("Semiannual Payment");
        } else if (mSpinPayFreq.getSelectedItemPosition() == 3) {
            val = 1;
            MonthlyPaymentTitle.setText("Annual Payment");
        }
        int months = 0;

        if (strMonth.length() == 0) {
            strMonth = "0";
        }
        months = (Integer.parseInt(str) * val) + (Integer.parseInt(strMonth));

        return months;
    }

    private int convertTerm(String str) {
        // TODO Auto-generated method stub

        int val = 1;
        if (mSpinPayFreq.getSelectedItemPosition() == 0) {
            val = 12;
        } else if (mSpinPayFreq.getSelectedItemPosition() == 1) {
            val = 4;
        } else if (mSpinPayFreq.getSelectedItemPosition() == 2) {
            val = 2;
        } else if (mSpinPayFreq.getSelectedItemPosition() == 3) {
            val = 1;
        }
        int months = 0;
        months = (Integer.parseInt(str) * val);

        return months;
    }

    public double calculatMonthlyPayment(double paramDouble1,
                                         double paramDouble2, int paramInt) {
        double d1 = paramDouble2 / 100.0D / 12.0D;
        double d2 = paramDouble1 * d1 * Math.pow(1.0D + d1, paramInt)
                / (Math.pow(1.0D + d1, paramInt) - 1.0D);
        if (d1 == 0.0D) {
            d2 = paramDouble1 / paramInt;
        }
        return Math.round(d2 * 100.0D) / 100.0D;
    }

}
