package mafia.adamzimny.mafia.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.daimajia.numberprogressbar.NumberProgressBar;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.api.ProgressRequestBody;
import mafia.adamzimny.mafia.api.RetrofitBuilder;
import mafia.adamzimny.mafia.api.service.ImgurService;
import mafia.adamzimny.mafia.api.service.UserService;
import mafia.adamzimny.mafia.constant.Params;
import mafia.adamzimny.mafia.model.User;
import mafia.adamzimny.mafia.model.UserTemplate;
import mafia.adamzimny.mafia.model.json.ImgurData;
import mafia.adamzimny.mafia.util.Validator;
import mafia.adamzimny.mafia.util.helper.DocumentHelper;
import mafia.adamzimny.mafia.util.helper.FaceHelper;
import mafia.adamzimny.mafia.util.helper.ImageLoaderHelper;
import mafia.adamzimny.mafia.util.helper.IntentHelper;
import retrofit2.Call;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, ProgressRequestBody.UploadCallbacks {

    UserTemplate template = new UserTemplate();

    @BindView(R.id.first_name_field)
    AutoCompleteTextView firstNameField;

    @BindView(R.id.last_name_field)
    AutoCompleteTextView lastNameField;

    @BindView(R.id.username_field)
    AutoCompleteTextView usernameField;
    @BindView(R.id.email_field)
    AutoCompleteTextView emailField;
    @BindView(R.id.password_field)
    EditText passwordField;

    @BindView(R.id.birthday_field)
    EditText birthdayField;

    @BindView(R.id.description_field)
    EditText descriptionField;

    @BindView(R.id.checkBox)
    CheckBox checkBox;

    @BindView(R.id.image_button)
    ImageButton imageButton;

    DatePickerDialog datePickerDialog;

    @BindView(R.id.button_register)
    Button registerButton;

    Call<ImgurData> uploadImageCall;

    @BindView(R.id.progress_bar)
    NumberProgressBar progressBar;
    Uri imageUri = null;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.face_detected)
    View faceDetected;
    @BindView(R.id.no_face_detected)
    View noFaceDetected;
    @BindView(R.id.multiple_faces_detected)
    View multipleFacesDetected;
    @BindView(R.id.face_too_small)
    View faceTooSmall;

    Bitmap croppedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        datePickerDialog = createDialog();
        setTitle("Register");
        ImageLoaderHelper.initialize(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        birthdayField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    datePickerDialog.show();
                }
            }
        });
        imageButton.requestFocus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @OnClick(R.id.button_register)
    public void startRegister() {
        template.setDateOfBirth(new Date(getBirthday(datePickerDialog.getDatePicker()).getTime()));
        template.setUsername(getUsername());
        template.setFirstName(getFirstName());
        template.setLastName(getLastName());
        template.setEmail(getEmail());
        template.setPassword(getPassword());
        Log.d("validator", template.toString());
        if (validateTemplate(template)) {
            RegisterTask task = new RegisterTask();
            task.execute();
        }
    }

    @OnClick(R.id.image_button)
    public void pickPhoto() {
        IntentHelper.chooseFileIntent(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentHelper.FILE_PICK && resultCode == RESULT_OK) {
            imageUri = data.getData();
            ImageLoader imageLoader = ImageLoader.getInstance();
            Log.d("filePicker", DocumentHelper.getPath(this, data.getData()));
            imageUri = data.getData();
            faceDetected.setVisibility(View.GONE);
            noFaceDetected.setVisibility(View.GONE);
            multipleFacesDetected.setVisibility(View.GONE);
            faceTooSmall.setVisibility(View.GONE);

            CropImage.activity(imageUri).setFixAspectRatio(true)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Log.d("crop", resultUri + " //// " + resultUri.getPath());
                ImageLoader imageLoader = ImageLoader.getInstance();
                imageLoader.displayImage("file://" + resultUri.getPath(), imageButton);

                try {
                    croppedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), resultUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FaceDetector faceDetector = new FaceDetector.Builder(this).build();
                Frame frame = new Frame.Builder().setBitmap(croppedImage).build();
                SparseArray<Face> faces = faceDetector.detect(frame);

                Log.d("faces", "detected faces: " + faces.size());
                faceDetector.release();

                if (faces.size() == 0) {
                    noFaceDetected.setVisibility(View.VISIBLE);
                } else if (faces.size() > 1) {
                    multipleFacesDetected.setVisibility(View.VISIBLE);
                    Log.d("faces", "face percent = " + FaceHelper.getFaceSizePercent(faces.get(0), croppedImage));

                } else if (FaceHelper.getFaceSizePercent(faces.get(0), croppedImage) < Params.FACE_PERCENTAGE) {
                    faceTooSmall.setVisibility(View.VISIBLE);
                    Log.d("faces", "face percent = " + FaceHelper.getFaceSizePercent(faces.get(0), croppedImage));
                } else {
                    faceDetected.setVisibility(View.VISIBLE);
                    Log.d("faces", "face percent = " + FaceHelper.getFaceSizePercent(faces.get(0), croppedImage));

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public DatePickerDialog createDialog() {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        birthdayField.setText(i + "/" + i1 + "/" + i2);
    }

    @Override
    public void onProgressUpdate(int percentage) {
        progressBar.setVisibility(View.VISIBLE);

        progressBar.setProgress(percentage);
       /* Log.d("upload", "update " + percentage);
        if (percentage == 0) {
            progressBar.setProgress(1);
        } else if (percentage != 99) {

        }*/
    }

    @Override
    public void onError() {
        progressBar.setVisibility(View.GONE);
        Log.d("upload", "error");
    }

    @Override
    public void onFinish() {
        Log.d("upload", "onFinish");
    }

    public Date getBirthday(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, datePicker.getDayOfMonth());
        calendar.set(Calendar.MONTH, datePicker.getMonth());
        calendar.set(Calendar.YEAR, datePicker.getYear());
        return calendar.getTime();

    }

    public String getFirstName() {

        String name = firstNameField.getText().toString();
        if (name.isEmpty())
            return "";

        String cap = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().trim();

        return cap;
    }

    public String getLastName() {
        String name = lastNameField.getText().toString();
        if (name.isEmpty())
            return "";
        String cap = name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase().trim();
        return cap;
    }

    public String getPassword() {
        return passwordField.getText().toString().trim();
    }

    public String getEmail() {
        return emailField.getText().toString().trim();
    }

    public String getUsername() {
        return usernameField.getText().toString().trim();

    }


    private boolean validateTemplate(UserTemplate userTemplate) {
        boolean ret = true;
        View focus = null;
        Log.d("validator", "validateTemplate " + userTemplate.toString() + "\n\n");
/*
        if (imageUri == null || !new File(DocumentHelper.getPath(this, imageUri)).exists()) {
            ret = false;
            Toast.makeText(this, "Pick an image", Toast.LENGTH_SHORT).show();
            Log.d("validator", "No image");

        }
*/

        if (Validator.isEmpty(userTemplate.getUsername())) {
            usernameField.setError("Username cannot be empty");
            Log.d("validator", "Username cannot be empty");
            ret = false;
        }

        if (Validator.isEmpty(userTemplate.getEmail())) {
            emailField.setError("Email cannot be empty");
            Log.d("validator", "Email cannot be empty");
            ret = false;
        }

        if (Validator.isEmpty(userTemplate.getLastName())) {
            lastNameField.setError("Name cannot be empty");
            Log.d("validator", "Name cannot be empty");
            ret = false;
        }

        if (Validator.isEmpty(userTemplate.getFirstName())) {
            firstNameField.setError("Name cannot be empty");
            Log.d("validator", "Name cannot be empty");
            ret = false;
        }

        if (Validator.isEmpty(userTemplate.getPassword())) {
            passwordField.setError("Password cannot be empty");
            Log.d("validator", "Password cannot be empty");
            ret = false;
        }

        if (!Validator.isCorrectName(userTemplate.getFirstName())) {
            firstNameField.setError("Please use only letters A-Z in name");
            Log.d("validator", "Please use only letters A-Z in name");
            ret = false;
        }
        if (!Validator.isCorrectName(userTemplate.getLastName())) {

            lastNameField.setError("Please use only letters A-Z in name");
            Log.d("validator", "Please use only letters A-Z in name");
            ret = false;
        }

        if (!Validator.isCorrectUsername(userTemplate.getUsername())) {
            usernameField.setError("Please use only letters, numbers or - _ in username");
            Log.d("validator", "Please use only letters, numbers or - _ in username");
            ret = false;

        }

        if (!Validator.isCorrectDate(userTemplate.getDateOfBirth())) {
            Log.d("validator", "Date of birth incorrect");
            ret = false;
        }
        if (!Validator.isCorrectEmail(userTemplate.getEmail())) {
            emailField.setError("Email incorrect");
            Log.d("validator", "Email incorrect");
            ret = false;
        }
        //  if (!ret) focus.requestFocus();
        return ret;
    }


    public class RegisterTask extends AsyncTask<Void, Void, User> {

        @Override
        protected void onPostExecute(User user) {
            progressBar.setVisibility(View.GONE);
            if (user != null) {
                Log.d("validator", user.toString());
                Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        finish();
                    }
                }, 3000);
            }
        }

        @SuppressWarnings("ConstantConditions")
        @Override
        protected User doInBackground(Void... voids) {
            ImgurService imgurService = (ImgurService) RetrofitBuilder.getService(ImgurService.class, RetrofitBuilder.IMGUR_URL);
            UserService userService = (UserService) RetrofitBuilder.getService(UserService.class, RetrofitBuilder.BASE_URL);
            Call<Boolean> usernameCall = userService.checkUsername(template.getUsername());
            Response<Boolean> usernameResponse;
            try {
                usernameResponse = usernameCall.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
         /*   if(!usernameResponse.body()){
                //TODO zrobić lepsze sprawdzanie dostepnosci username, na przycisk np.
                return null;
            }*/
            File file = null;
            try {

                file = new File(RegisterActivity.this.getCacheDir(), "temp_profile");
                file.createNewFile();

//Convert bitmap to byte array

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                croppedImage.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(bitmapdata);
                fos.flush();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            ProgressRequestBody pBody = new ProgressRequestBody(file, RegisterActivity.this);

            uploadImageCall = imgurService.uploadImage(pBody);
            Response<ImgurData> imgurResponse;
            try {
                imgurResponse = uploadImageCall.execute();
            } catch (IOException e) {
                return null;
            }
            template.setProfilePicture(imgurResponse.body().getData().getId());
            //  RequestBody body = RequestBody.create(MediaType.parse("application/json"),template);
            Call<User> userCall = userService.register(template);
            Response<User> registeredUser;
            try {
                registeredUser = userCall.execute();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            Log.d("call", "call status =" + registeredUser.code() + ", message =" + registeredUser.message());
            return registeredUser.body();
        }
    }
}
