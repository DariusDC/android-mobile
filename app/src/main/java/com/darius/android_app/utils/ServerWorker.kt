import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.darius.android_app.AndroidApplication
import com.darius.android_app.model.Hotel

class ServerWorker(
    context: Context,
    val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        val itemRepository =(applicationContext as AndroidApplication).container.hotelRepository;

        val isSaving = workerParams.inputData.getBoolean("isSaving", true);
        val id = workerParams.inputData.getString("id")!!
        val name = workerParams.inputData.getString("name")!!
        val price = workerParams.inputData.getDouble("price", 0.0)!!;
        val desc = workerParams.inputData.getString("desc")!!;
        val added = workerParams.inputData.getString("added")!!;
        val available = workerParams.inputData.getBoolean("available", false);
//        val imageUri = workerParams.inputData.getString("imageUri")!!;


        val item = Hotel(id, name, price, desc, added, available, "", null)

        if(isSaving) {
            itemRepository.save(item)
        }

        return Result.success()
    }
}