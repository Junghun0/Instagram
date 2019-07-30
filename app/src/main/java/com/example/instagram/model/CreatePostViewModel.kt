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

    fun createPost(post: Post): Void {
//        isProgress.postValue(true)
        val db = FirebaseFirestore.getInstance()

        val ref = db.collection("insta_posts").document()
        post.uid = ref.id

        return Tasks.await(ref.set(post).addOnCompleteListener {
//            isProgress.postValue(false)
        })
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

/*    suspend fun uploadPostAsync(stream: InputStream, text: String) {
        isProgress.postValue(true)
        val ref = FirebaseStorage.getInstance().reference
            .child("images/${System.currentTimeMillis()}.jpg")

        ref.putStream(stream).await()

        val downloadUri = ref.downloadUrl.await()

        val post = Post(
            "a811219@gmail.com",
            FirebaseAuth.getInstance().currentUser?.email,
            FirebaseAuth.getInstance().currentUser?.photoUrl?.toString(),
            downloadUri.toString(),
            text
        )

        db.collection("insta_posts").add(post).await()

        isProgress.postValue(false)
    }*/