package com.example.grocerysystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rajat.pdfviewer.PdfViewerActivity;

public class TestingBe extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing_be);
        startActivity(

                // Opening pdf from assets folder

                PdfViewerActivity.Companion.launchPdfFromUrl(
                        this,
                        "https://apps-s3-prod.utkarshapp.com/admin_v1/file_manager/pdf/277364772914085660_PDF.pdf",
                        "",
                        "",
                        false
                )
        );
    }
}