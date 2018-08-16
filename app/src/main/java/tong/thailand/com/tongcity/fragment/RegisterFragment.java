package tong.thailand.com.tongcity.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import tong.thailand.com.tongcity.MainActivity;
import tong.thailand.com.tongcity.R;
import tong.thailand.com.tongcity.utility.MyAlert;

public class RegisterFragment extends Fragment {
    //   Explicit
    private Uri uri;
    private boolean aBoolean = true;
    private  String nameString, emaiString, passString;



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Create Toolbar
        createToolbar();

//        Avatar Controller
        avatarController();
    } // Method

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==getActivity().RESULT_OK) {
            aBoolean = false;
            uri = data.getData();
            showAvata();

        } else {
            Toast.makeText(getActivity(),"Please Choose Image", Toast.LENGTH_SHORT).show();
        }


    }

    private void showAvata() {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
            ImageView imageView = getView().findViewById(R.id.imAvatar);

            Bitmap bitmap1 = Bitmap.createScaledBitmap(bitmap, 800, 480, true);
            imageView.setImageBitmap(bitmap1);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void avatarController() {
        ImageView imageView = getView().findViewById(R.id.imAvatar);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "Choose Image"),1);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId()== R.id. itemUpload) {
            uploadAvatar();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void uploadAvatar() {

        EditText nameEditText = getView().findViewById(R.id.edtName);
        EditText emailEditText = getView().findViewById(R.id.edtEmail);
        EditText passwordEditText = getView().findViewById(R.id.edtPassword);

        nameString = nameEditText.getText().toString().trim();
        emaiString = emailEditText.getText().toString().trim();
        passString = passwordEditText.getText().toString().trim();

        if (aBoolean) {
            MyAlert myAlert = new MyAlert(getActivity());
            myAlert.normalDialog("Non Image","Please Choose Image");

        } else if (nameString.isEmpty() || emaiString.isEmpty() || passString.isEmpty()) {
            MyAlert myAlert = new MyAlert(getActivity());
            myAlert.normalDialog("Have Space","Please Fill All Bank");
        } else {
            registerFirebase();
        }


    }

    private void registerFirebase() {

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(emaiString, passString)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            UploadPicture();
                        } else {
                            MyAlert myAlert = new MyAlert(getActivity());
                            myAlert.normalDialog("Register False",task
                            .getException().getMessage());
                        }
                    }
                });


    }

    private void UploadPicture() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();

        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference()
                .child("Avatar/" + nameString);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                progressDialog.dismiss();
            }
        });



    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);


        inflater.inflate(R.menu.menu_register, menu);
    }

    private void createToolbar() {
        Toolbar toolbar = getView().findViewById(R.id.toobarRegister);
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.register);
        ((MainActivity) getActivity()).getSupportActionBar().setSubtitle("Please Fill in Blank");
        ((MainActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        return view;
    }
}
