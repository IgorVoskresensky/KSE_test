package ru.ivos.kse_test.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.ivos.kse_test.databinding.ItemCarBinding
import ru.ivos.kse_test.domain.model.Car

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    inner class HomeViewHolder(
        private val binding: ItemCarBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(car: Car) {
            binding.apply {
                Glide.with(itemView).load(car.image).into(ivCar)
                tvModel.text = car.model
                tvManufacturer.text = car.manufacturer
                tvPrice.text = "$${car.price}"
                tvTransmission.text = car.transmission
                tvYear.text = car.year.toString()
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(car)
                    }
                }
            }
        }
    }

    private val differCallback = object : DiffUtil.ItemCallback<Car>() {
        override fun areItemsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem.model == newItem.model
        }

        override fun areContentsTheSame(oldItem: Car, newItem: Car): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemCarBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val movieDetails = differ.currentList[position]
        holder.bind(movieDetails)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Car) -> Unit)? = null

    fun setOnItemClickListener(listener: (Car) -> Unit) {
        onItemClickListener = listener
    }
}