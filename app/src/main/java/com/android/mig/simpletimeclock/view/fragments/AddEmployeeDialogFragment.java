package com.android.mig.simpletimeclock.view.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.mig.simpletimeclock.R;
import com.android.mig.simpletimeclock.view.ui.EmployeeDetailsActivity;
import com.android.mig.simpletimeclock.view.utils.CircleTransform;
import com.bumptech.glide.Glide;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickClick;

import static android.app.Activity.RESULT_OK;

public class AddEmployeeDialogFragment extends DialogFragment {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_PHOTO_PICKER = 2;
    private static final int MY_PERMISSIONS_REQUEST_USE_CAMERA = 10;        // code should be bigger than 0
    private static final String PHOTO_SAVED_STATE_ID = "photo_path";

    private NoticeDialogListener mNoticeDialogListener;
    private PhotoPickerListener mPhotoPickerListener;

    private TextView mTitleTextView;
    private ImageView mBlankImageView;
    private EditText mNameEditText;
    private EditText mWageEditText;
    private Uri mPhotoPath, mTempContainerPath;
    private PickImageDialog mPickImageDialog;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mNoticeDialogListener = (NoticeDialogListener) context;
            mPhotoPickerListener = (PhotoPickerListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString() + " must implement NoticeDialogListener");
        }
    }

    /**
     * Necessary deprecated method for older APIs
     *
     * @param activity host activity
     */
    @SuppressWarnings("deprecation")
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mNoticeDialogListener = (NoticeDialogListener) activity;
            mPhotoPickerListener = (PhotoPickerListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View rootView = inflater.inflate(R.layout.add_employee_dialog, null);
        mTitleTextView = rootView.findViewById(R.id.dialog_title_text_view);
        mBlankImageView = rootView.findViewById(R.id.blank_image_view);
        mNameEditText = rootView.findViewById(R.id.name_edit_text);
        mWageEditText = rootView.findViewById(R.id.wage_edit_text);

        mBlankImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                PickSetup pickSetup = new PickSetup()
                        .setTitle(getResources().getString(R.string.dialog_add_photo_title))
                        .setSystemDialog(false);

                mPickImageDialog = PickImageDialog.build(pickSetup)
                        .setOnClick(new IPickClick() {
                            @Override
                            public void onGalleryClick() {
                                mPickImageDialog.dismiss();
                                dispatchGalleryIntent();
                            }

                            @Override
                            public void onCameraClick() {
                                checkWritePermission();
                                mPickImageDialog.dismiss();
                            }
                        }).show((FragmentActivity) getActivity());
            }
        });

        if (savedInstanceState != null) {
            String savedPhotoPath = savedInstanceState.getString(PHOTO_SAVED_STATE_ID);
            if (savedPhotoPath != null){
                setPhoto(savedPhotoPath);
            } else {
                setPhoto("");
            }
        } else {
            // if dialog was invoked from detail activity
            if (getArguments() != null) {
                mTitleTextView.setText(getResources().getString(R.string.dialog_title_text_edit));
                String name = getArguments().getString(EmployeeDetailsActivity.EMP_NAME_TAG);
                String wage = String.valueOf(getArguments().getDouble(EmployeeDetailsActivity.EMP_WAGE_TAG));
                String photoPath = getArguments().getString(EmployeeDetailsActivity.EMP_PHOTOPATH_TAG);
                mNameEditText.setText(name);
                mWageEditText.setText(wage);
                setPhoto(photoPath);
            } else {
                mTitleTextView.setText(getResources().getString(R.string.dialog_title_text_new));
                setPhoto("");
            }
        }

        builder.setView(rootView)
                // Add action buttons
                .setPositiveButton(R.string.dialog_accept_button, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // see action on onResume method which prevents
                        // closing the dialog if EditText fields are empty
                    }
                })
                .setNegativeButton(R.string.dialog_cancel_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }

    @Override
    public void onResume() {
        super.onResume();
        final AlertDialog alertDialog = (AlertDialog) getDialog();
        if (alertDialog != null) {
            Button positiveButton = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = mNameEditText.getText().toString().trim();
                    String wage = mWageEditText.getText().toString().trim();
                    if (!name.isEmpty() && !wage.isEmpty()) {
                        mPhotoPickerListener.onPhotoTaken(String.valueOf(mPhotoPath));
                        mNoticeDialogListener.onDialogPositiveClick(AddEmployeeDialogFragment.this);
                        alertDialog.dismiss();
                    }
                }
            });

            Button negativeButton = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.cancel();
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPhotoPath != null){
            outState.putString(PHOTO_SAVED_STATE_ID, String.valueOf(mPhotoPath));
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // receives result code from camera intent launched previously
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            setPhoto(String.valueOf(mTempContainerPath));
            mTempContainerPath = null;
        } else if (requestCode == REQUEST_PHOTO_PICKER && resultCode == RESULT_OK) {
            setPhoto(String.valueOf(data.getData()));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_USE_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    dispatchCameraIntent();
                }
            }
        }
    }

    private void checkWritePermission() {
        int cameraPermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
        int writePermissionCheck = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (cameraPermissionCheck == PackageManager.PERMISSION_DENIED || writePermissionCheck == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_USE_CAMERA);
        } else if (cameraPermissionCheck == PackageManager.PERMISSION_GRANTED && writePermissionCheck == PackageManager.PERMISSION_GRANTED) {
            dispatchCameraIntent();
        }
    }

    /**
     * Launches camera intent with a temporary path
     */
    public void dispatchCameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "");
            mTempContainerPath = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTempContainerPath);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void dispatchGalleryIntent() {
        Intent intent = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), REQUEST_PHOTO_PICKER);
    }

    /**
     * Sets the ImageView for either a placeholder or an actual picture
     *
     * @param photoPath path of picture if there is any
     */
    private void setPhoto(String photoPath){
        if (photoPath.equals("null") || photoPath.isEmpty()){
            Glide.with(getActivity().getApplicationContext())
                    .load(R.drawable.im_blank_profile)
                    .transform(new CircleTransform(getActivity().getApplicationContext()))
                    .into(mBlankImageView);
        } else {
            mPhotoPath = Uri.parse(photoPath);
            Glide.with(mBlankImageView.getContext())
                    .load(mPhotoPath)
                    .transform(new CircleTransform(mBlankImageView.getContext()))
                    .into(mBlankImageView);
        }

    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(DialogFragment dialog);

        void onDialogNegativeClick(DialogFragment dialog);
    }

    public interface PhotoPickerListener {
        void onPhotoTaken(String photoPath);
    }
}
