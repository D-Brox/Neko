package eu.kanade.tachiyomi.ui.reader.settings

import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.google.android.material.tabs.TabLayout
import eu.kanade.tachiyomi.ui.main.MainActivity
import eu.kanade.tachiyomi.ui.reader.ReaderActivity
import eu.kanade.tachiyomi.util.view.collapse
import eu.kanade.tachiyomi.util.view.expand
import eu.kanade.tachiyomi.util.view.isCollapsed
import eu.kanade.tachiyomi.widget.TabbedBottomSheetDialog
import org.nekomanga.R
import org.nekomanga.databinding.ReaderColorFilterBinding

class TabbedReaderSettingsSheet(
    val readerActivity: ReaderActivity,
    showColorFilterSettings: Boolean = false,
) : TabbedBottomSheetDialog(readerActivity) {
    private val generalView: ReaderGeneralView =
        View.inflate(readerActivity, R.layout.reader_general_layout, null) as ReaderGeneralView
    private val pagedView: ReaderPagedView =
        View.inflate(readerActivity, R.layout.reader_paged_layout, null) as ReaderPagedView
    private val filterView: ReaderFilterView =
        View.inflate(readerActivity, R.layout.reader_color_filter, null) as ReaderFilterView

    var showWebtoonView: Boolean = run {
        val mangaViewer = readerActivity.viewModel.getMangaReadingMode()
        ReadingModeType.isWebtoonType(mangaViewer)
    }

    override var offset = 0

    override fun getTabViews(): List<View> = listOf(generalView, pagedView, filterView)

    override fun getTabTitles(): List<Int> =
        listOf(
            R.string.general,
            if (showWebtoonView) R.string.webtoon else R.string.paged,
            R.string.filter,
        )

    init {
        generalView.activity = readerActivity
        pagedView.activity = readerActivity
        filterView.activity = readerActivity
        filterView.window = window
        generalView.sheet = this

        ReaderColorFilterBinding.bind(filterView).swipeDown.setOnClickListener {
            if (sheetBehavior.isCollapsed()) {
                sheetBehavior.expand()
            } else {
                sheetBehavior.collapse()
            }
        }

        binding.menu.isVisible = true
        binding.menu.tooltipText = context.getString(R.string.reader_settings)
        binding.menu.setImageDrawable(
            ContextCompat.getDrawable(context, R.drawable.ic_outline_settings_24dp)
        )
        binding.menu.setOnClickListener {
            val intent = MainActivity.openReaderSettings(readerActivity)
            readerActivity.startActivity(intent)
            dismiss()
        }

        val filterTabIndex = getTabViews().indexOf(filterView)
        binding.pager.adapter?.notifyDataSetChanged()
        binding.tabs.addOnTabSelectedListener(
            object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    val isFilterTab = tab?.position == filterTabIndex

                    // Remove dimmed backdrop so color filter changes can be previewed
                    if (isFilterTab) {
                        window?.setDimAmount(0f)
                    } else {
                        window?.setDimAmount(0.25f)
                    }
                    readerActivity.binding.appBar.isInvisible = tab?.position == filterTabIndex
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}

                override fun onTabReselected(tab: TabLayout.Tab?) {}
            }
        )

        if (showColorFilterSettings) {
            window?.setDimAmount(0f)
            binding.tabs.getTabAt(filterTabIndex)?.select()
        }
    }

    override fun show() {
        super.show()
        window?.setDimAmount(0.25f)
    }

    override fun hide() {
        super.hide()
    }

    override fun dismiss() {
        super.dismiss()
    }

    fun updateTabs(isWebtoon: Boolean) {
        showWebtoonView = isWebtoon
        binding.pager.adapter?.notifyDataSetChanged()
        pagedView.updatePrefs()
    }
}
