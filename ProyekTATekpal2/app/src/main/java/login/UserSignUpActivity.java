package login;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.prasetyon.proyektatekpal2.R;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import dokumen.FilePath;

import static dokumen.UploadFileActivity.UPLOAD_URL;

public class UserSignUpActivity extends AppCompatActivity {

    LinearLayout llSignature;
    Button btnBack, btnRegister, btnReset, btnChoose;
    Button btnClear;
    DrawingView dv ;
    ScrollView svData;
    private Paint mPaint;
    EditText edUsername, edPassword, edVerPassword, edFullName, edEmail, edPhoneNumber, edActivationCode, edNIK, edFile;
    Spinner spinnerDepartment;
    String department="";
    final String KODE_AKTIVASI = "TEKPAL";
    Context activity;
    boolean signedUp=false;
    String signed;
    private Uri filePath;
    //Pdf request code
    private int PICK_PDF_REQUEST = 1;

    //storage permission code
    private static final int STORAGE_PERMISSION_CODE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_sign_up);
        setTitle(R.string.user_reg);
        activity = this;

        btnBack = (Button) findViewById(R.id.btnBack);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnReset = (Button) findViewById(R.id.btnReset);
        edUsername = (EditText) findViewById(R.id.edUsername);
        edPassword = (EditText) findViewById(R.id.edPassword);
        edVerPassword = (EditText) findViewById(R.id.edVerPassword);
        edFullName = (EditText) findViewById(R.id.edFullName);
        edEmail = (EditText) findViewById(R.id.edEmail);
        edPhoneNumber = (EditText) findViewById(R.id.edPhoneNumber);
        edActivationCode = (EditText) findViewById(R.id.edActivationCode);
        edNIK = (EditText) findViewById(R.id.edNIK);
        edFile = (EditText) findViewById(R.id.edFile);

        llSignature = (LinearLayout) findViewById(R.id.llSignature);
        btnChoose = (Button) findViewById(R.id.btnChoose);
        btnClear = (Button) findViewById(R.id.btnClear);
        svData = (ScrollView) findViewById(R.id.svData);

        dv = new DrawingView(this);
        llSignature.addView(dv);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);

        spinnerDepartment = (Spinner) findViewById(R.id.spinnerDepartment);
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.department_array, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinnerDepartment.setAdapter(spinnerAdapter);
        spinnerDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = spinnerDepartment.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dv.clearDrawing();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserSignUpActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edUsername.setText("");
                edPassword.setText("");
                edVerPassword.setText("");
                edFullName.setText("");
                edEmail.setText("");
                edPhoneNumber.setText("");
                edNIK.setText("");
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Register new account here, then go to HOME (Role: User)
                /* have to chek whether the password is the same or not,
                   and whether the account is succesfully registered or not*/
                if(edPassword.getText().toString().equals(edVerPassword.getText().toString()) && edActivationCode.getText().toString().equals(KODE_AKTIVASI)) {
                    String username = edUsername.getText().toString();
                    String name = edFullName.getText().toString();
                    String password = edPassword.getText().toString();
                    String email = edEmail.getText().toString();
                    String phone = edPhoneNumber.getText().toString();
                    String nik = edNIK.getText().toString();
                    new BackgroundTask().execute(username, password, name, department, email, phone, nik);
                }
                else if(!edPassword.getText().toString().equals(edVerPassword.getText().toString())){
                    Toast.makeText(UserSignUpActivity.this, "Login Failed, Password didn't match!", Toast.LENGTH_SHORT).show();
                }
                else if(!edActivationCode.getText().toString().equals(KODE_AKTIVASI)){
                    Toast.makeText(UserSignUpActivity.this, "Login Failed, Wrong activation code!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class BackgroundTask extends AsyncTask<String, String, String> {
        android.app.ProgressDialog ProgressDialog;

        public BackgroundTask(){
            ProgressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ProgressDialog.setMessage("Signing up...");
            ProgressDialog.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            signedUp = true;
            //Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            ProgressDialog.dismiss();
            if(signedUp && signed.equals("1")){
                uploadMultipartFile(edUsername.getText().toString());
                printDocument();
                //Toast.makeText(UserSignUpActivity.this, s, Toast.LENGTH_SHORT).show();
                Toast.makeText(UserSignUpActivity.this, "You have been signed up! Login to continue", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(UserSignUpActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
            else Toast.makeText(UserSignUpActivity.this, s, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuilder stringBuilder = new StringBuilder();

            String username = params[0];
            String password = params[1];
            String name = params[2];
            String department = params[3];
            String email = params[4];
            String phone = params[5];
            String nik = params[6];

            String create_url = "http://mobile4day.com/welding-inspection/new_user.php";
            try {
                URL url = new URL(create_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);

                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String data =
                        URLEncoder.encode("username", "UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"+
                                URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                                URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                                URLEncoder.encode("department","UTF-8")+"="+URLEncoder.encode(department,"UTF-8")+"&"+
                                URLEncoder.encode("email","UTF-8")+"="+URLEncoder.encode(email,"UTF-8")+"&"+
                                URLEncoder.encode("number","UTF-8")+"="+URLEncoder.encode(phone,"UTF-8")+"&"+
                                URLEncoder.encode("nik","UTF-8")+"="+URLEncoder.encode(nik,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String response = "";
                String line = "";
                while((line = bufferedReader.readLine())!= null){
                    response += line;
                }
                signed = response;
                bufferedReader.close();
                inputStream.close();

                httpURLConnection.disconnect();
                return response;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return stringBuilder.toString().trim();
        }
    }

    public class DrawingView extends View {

        public int width;
        public int height;
        private Bitmap mBitmap;
        private Canvas mCanvas;
        private Path mPath;
        private Paint mBitmapPaint;
        Context context;
        private Paint circlePaint;
        private Path circlePath;

        public DrawingView(Context c) {
            super(c);
            context = c;
            mPath = new Path();
            mBitmapPaint = new Paint(Paint.DITHER_FLAG);
            circlePaint = new Paint();
            circlePath = new Path();
            circlePaint.setAntiAlias(true);
            circlePaint.setColor(Color.BLACK);
            circlePaint.setStyle(Paint.Style.STROKE);
            circlePaint.setStrokeJoin(Paint.Join.MITER);
            circlePaint.setStrokeWidth(4f);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            mCanvas = new Canvas(mBitmap);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
            canvas.drawPath(mPath, mPaint);
            canvas.drawPath(circlePath, circlePaint);
        }

        private float mX, mY;
        private static final float TOUCH_TOLERANCE = 4;

        private void touch_start(float x, float y) {
            mPath.reset();
            mPath.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void touch_move(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
                mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;

                circlePath.reset();
                circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
            }
        }

        private void touch_up() {
            mPath.lineTo(mX, mY);
            circlePath.reset();
            // commit the path to our offscreen
            mCanvas.drawPath(mPath, mPaint);
            // kill this so we don't double draw
            mPath.reset();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    touch_start(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    touch_move(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    touch_up();
                    invalidate();
                    break;
            }
            return true;
        }

        public void clearDrawing() {
            //Toast.makeText(UserSignUpActivity.this, "You want to clear them all!", Toast.LENGTH_SHORT).show();
            mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
            invalidate();
        }
    }

    public void printDocument()
    {
        Calendar c = Calendar.getInstance();

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c.getTime());
        btnRegister.setVisibility(View.INVISIBLE);
        LinearLayout content = (LinearLayout) findViewById(R.id.llSignature);
        content.setDrawingCacheEnabled(true);
        Bitmap bitmap = content.getDrawingCache();
        File file,f=null;
        String x = "Signature.png";
        try{
            if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            {
                file =new File(android.os.Environment.getExternalStorageDirectory(),"Welding Inspection Report");
                if(!file.exists())
                {
                    file.mkdirs();

                }
                f = new File(file, x);
            }
            FileOutputStream ostream = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 10, ostream);
            //Toast.makeText(UserSignUpActivity.this, "Your report has been saved", Toast.LENGTH_SHORT).show();
            ostream.close();
            uploadMultipart(x, edUsername.getText().toString());
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void uploadMultipart(String pathName, String name) {
        //getting name for the image

        //getting the actual path of the image
        filePath = Uri.parse("/storage/emulated/0/Welding Inspection Report/" + pathName);
        String path = filePath.toString();
        //Toast.makeText(InspeksiFinalReportActivity.this, "Path: " + path, Toast.LENGTH_LONG).show();

        if (path == null) {
            Toast.makeText(this, "Please move your file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("proses", "SIGNATURE") //Adding text parameter to the request
                        .addParameter("name", name) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void uploadMultipartFile(String name) {
        //getting the actual path of the image
        String path = FilePath.getPath(this, filePath);
        //Toast.makeText(UploadFileActivity.this, "Path: " + path, Toast.LENGTH_LONG).show();

        if (path == null) {
            Toast.makeText(this, "Please move your file to internal storage and retry (2)", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "pdf") //Adding file
                        .addParameter("proses", "SERTIF") //Adding text parameter to the request
                        .addParameter("name", name) //Adding text parameter to the request
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload

            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //method to show file chooser
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select file"), PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            edFile.setText(filePath.toString());
        }
    }


    //Requesting permission
    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }


    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }
}
