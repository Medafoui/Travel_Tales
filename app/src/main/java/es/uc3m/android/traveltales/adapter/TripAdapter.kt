package es.uc3m.android.traveltales.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import es.uc3m.android.traveltales.R

// Class that retrieves that provides views for the list of trips contained in MyTripsFragment
class TripAdapter(var tripList: List<Map<String, Any>>) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tripName: TextView = itemView.findViewById(R.id.friend_name)
        val tripDescription: TextView = itemView.findViewById(R.id.friend_trip)
        val tripImageView: ImageView = itemView.findViewById(R.id.trip_background) // replace with your ImageView id
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.my_trip, parent, false)
        return TripViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val currentItem = tripList[position]
        holder.tripName.text = currentItem["tripName"].toString()
        holder.tripDescription.text = currentItem["tripDescription"].toString()
        val tripImageUri = currentItem["tripImageUri"].toString()

        Glide.with(holder.itemView.context)
            .load(tripImageUri)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    Log.e("TripAdapter", "Load failed", e)
                    return false // let Glide handle the rest
                }
                override fun onResourceReady(resource: Drawable?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                    return false // let Glide handle the rest
                }
            })
            .into(holder.tripImageView)
    }
    override fun getItemCount() = tripList.size
}