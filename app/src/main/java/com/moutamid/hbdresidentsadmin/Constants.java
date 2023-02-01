package com.moutamid.hbdresidentsadmin;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Constants {

    public static FirebaseAuth auth() {
        return FirebaseAuth.getInstance();
    }

    public static DatabaseReference databaseReference() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("HBD_Residents");
        db.keepSynced(true);
        return db;
    }
    /*public static StorageReference storageReference(String auth){
        return FirebaseStorage.getInstance().getReference().child("HBD_Residents").child(auth);
    }*/
}
