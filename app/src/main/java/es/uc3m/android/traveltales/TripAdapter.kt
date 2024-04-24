
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import es.uc3m.android.traveltales.R


// class is where you define the views that you want to manipulate in each item of your RecyclerView
class TripAdapter(private val tripList: List<Map<String, Any>>) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tripName: TextView = itemView.findViewById(R.id.friend_name)
        val tripDescription: TextView = itemView.findViewById(R.id.friend_trip)
//        val tripBackground: ImageView = itemView.findViewById(R.id.trip_background)

        // Add other views for tripStartDate and tripEndDate
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_trip, parent, false)
        return TripViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val currentItem = tripList[position]
        holder.tripName.text = currentItem["tripName"].toString()
        holder.tripDescription.text = currentItem["tripDescription"].toString()

//        // Get the image URL from the currentItem
//        val imageUrl = currentItem["imageUrl"].toString()
//
//        // Use Glide to load the image from the URL
//        Glide.with(holder.itemView)
//            .load(imageUrl)
//            .into(holder.tripBackground)
//        // Set text for tripStartDate and tripEndDate
    }

    override fun getItemCount() = tripList.size
}