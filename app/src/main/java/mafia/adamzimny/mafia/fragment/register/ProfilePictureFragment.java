package mafia.adamzimny.mafia.fragment.register;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.system.ErrnoException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import mafia.adamzimny.mafia.R;
import mafia.adamzimny.mafia.fragment.RegisterFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by adamz on 19.08.2016.
 */
public class ProfilePictureFragment extends RegisterFragment {

    @BindView(R.id.image_view)
    ImageView imageView;

    Uri profilePictureURI;

    public ProfilePictureFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_picture_step, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.gallery_button)
    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1234);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1234 && resultCode == Activity.RESULT_OK) {
            if (data == null) {

                return;
            }
            imageView.setImageURI(data.getData());
            profilePictureURI = data.getData();
            //Now you can do whatever you want with your inpustream, save it as file, upload to a server, decode a bitmap...
        }
    }

    @Override
    public void getTemplateInfo() {
        //TODO update user template with image
    //    getUserTemplate().setProfilePicture(getProfilePicturePath());
    }


}
