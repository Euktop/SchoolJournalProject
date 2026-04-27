package stud.euktop.uikit.components.input.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import stud.euktop.uikit.components.adapter.SchJTextAdapter
import stud.euktop.uikit.components.bottomsheet.SchJBottomSheet
import stud.euktop.uikit.databinding.ItemListContentBinding

class SchJSelectSheet<T>(
    private val adapter: SchJTextAdapter<T>
) : SchJBottomSheet() {
    private var binding: ItemListContentBinding? = null
    override fun provideContentView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ItemListContentBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.root?.apply {
            layoutManager = LinearLayoutManager(requireContext())
            this.adapter = this@SchJSelectSheet.adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}