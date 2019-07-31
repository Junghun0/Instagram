package com.example.instagram.model

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.io.InputStream


class CreatePostViewModel: ViewModel(){

    private val db = FirebaseFirestore.getInstance()
    val isProgress = MutableLiveData<Boolean>()

    init {
        isProgress.value = false
    }

    fun uploadImage(stream: InputStream): Uri {
        isProgress.postValue(true)
        val ref = FirebaseStorage.getInstance().reference
            .child("images/${System.currentTimeMillis()}.jpg")

        return Tasks.await(ref.putStream(stream).continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>>{
            task ->
            if (!task.isComplete){
                task.exception?.let{
                    throw it
                }
            }
            isProgress.postValue(false)
            return@Continuation ref.downloadUrl
        }))
    }

    fun createPost(post: Post) {
        isProgress.postValue(true)
        val db = FirebaseFirestore.getInstance()

        val ref = db.collection("insta_posts").document()
        post.uid = ref.id

        ref.set(post).addOnCompleteListener {
            isProgress.postValue(false)
        }
    }

    fun updatePost(post: Post, callback: () -> Unit) {
        isProgress.postValue(true)

        db.collection("insta_posts").document(post.uid).set(post).addOnCompleteListener {
            isProgress.postValue(false)
            callback.invoke()
        }
    }

    fun deletePost(post: Post, callback: () -> Unit){
        isProgress.postValue(true)

        db.collection("insta_posts").document(post.uid).delete().addOnCompleteListener {
            isProgress.postValue(false)
            callback.invoke()
        }
    }
}