package com.example.ecoapp.feature.simplelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.ecoapp.Constants
import com.example.ecoapp.R
import com.example.ecoapp.data.News
import com.example.ecoapp.data.SimpleListItem
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_simple_list.view.*

class SimpleListAdapter(private val requestManager: RequestManager? = null) :
    RecyclerView.Adapter<SimpleListAdapter.SimpleHolder>() {

    val news =  News(-1,"Подготовка: Пластик",fullText =  "Пластик рекомендуется ополоснуть от остатков пищи, жира. Если на акциях принимают пластик разного типа, то лучше разделить его по видам (Какие виды пластика есть и как его делить см в прикрепленных статьях).  \n" +
            "\n" +
            "Пластик также стоит сократить до минимального объема: например, бутылки должны быть смяты. С них также можно снять крышки. Если этикетки с бутылок легко снимаются, то желательно их также снять. Нельзя заполнять пластиковые бутылки другим пластиком (пакетами и т.п.).\n" +
            "\n" +
            "Пакеты и пленку разделить на два вида: только бесцветные, без рисунков и краски и все остальное.",
        allPhotos = listOf("https://cdn.recyclemag.ru/main/a/a02173b78f7ea8014f9af461ef73e66f.jpg","https://cdn.recyclemag.ru/content/4/49280edc4b4d58ac6668f00c2315d0ac.jpg"))
    val news2 =  News(-2,"Подготовка: Какие есть виды пластика",fullText =  "Какие есть виды пластика\n" +
            "«Самые распространенные виды пластика – это ПЭТ (полиэтилентерефталат), ПНД, ПВД (полиэтилен низкого и высокого давления), ПВХ (поливинилхлорид), полистирол, полипропилен. У каждого из этих видов есть своя цифровая маркировка — от одного до шести. Другие виды пластика принято обозначать цифрой семь.\n" +
            "\n" +
            "Пластик различается по свойствам и делится на относительно безопасные виды для человека (например, одним из самых безопасных считается полипропилен) и потенциально опасные (полистирол при нагревании выделяет потенциально канцерогенное вещество стирол).\n" +
            "\n" +
            "\n" +
            "Несмотря на потенциальный вред для человека, эти виды пластика активно используются как упаковка для пищевых продуктов: из полистирола («6») делают крышки для горячих напитков навынос, подложки для фруктов, мяса, кондитерских изделий.\n" +
            "\n" +
            "Пластиковые отходы – востребованное на рынке сырье. Но в работе с ним есть сложности: его трудно собрать отдельно по видам, он объемен и часто загрязнен органическими отходами.\n" +
            "\n" +
            "«Большие транспортные расходы и необходимость досортировки снижают привлекательность этого вида вторичного сырья для заготовителей. Кроме того, есть пластик, который практически не перерабатывается в России: у нас сложно сдать на переработку опасный ПВХ («3»), а также композитные материалы и металлизированную пленку (в такой продаются, например, чипсы)», — рассказывает Мария.\n" +
            "\n" +
            "Как определить вид пластика\n" +
            "На любом пластиковом изделии, будь то бутылка из-под воды, флакон для шампуня или подложка для мяса, должна быть маркировка – цифра внутри треугольника или рядом с ним. На изделии может присутствовать буквенное обозначение на латинице или кириллице. Возьмите любую пластиковую упаковку и внимательно ее рассмотрите: вы найдете, к какому виду пластика она относится.\n" +
            "\n" +
            "Что делать, если на упаковке нет маркировки\n" +
            "Отсутствие маркировки может говорить о недобросовестности производителя и непредсказуемом составе изделия, которое может испортить целую партию вторичного сырья. Поэтому пластик без маркировки лучше бросить в контейнер для смешанного мусора. А совершая покупки, обращать внимание на наличие маркировки у пластика.\n" +
            "\n" +
            "Почему разные виды пластика нужно сдавать отдельно\n" +
            "«Разные виды пластика перерабатываются при разной температуре, в разных условиях и поэтому часто вообще на разных заводах. Если расплавить пластиковый микс, ничего путного из него сделать не получится. Есть пластики, которые можно смешивать, но обычно это ведет к утрате их изначальных свойств», — рассказывает Мария.",
        allPhotos = listOf("https://www.asi.org.ru/wp-content/uploads/2019/05/RIAN_5758011.LR_.ru_.jpg","https://www.asi.org.ru/wp-content/uploads/2019/05/bottle-940001_1920.jpg","https://www.asi.org.ru/wp-content/uploads/2019/05/plastic-bottles-115071_1920.jpg"))

    val list = arrayListOf(
        SimpleListItem("Пластик", imgRes = R.drawable.icon_plastic, news = news.apply { attachedNews = arrayListOf(news2) }),
        SimpleListItem("Бумага", imgRes =  R.drawable.icon_paper),
        SimpleListItem("Стекло", imgRes = R.drawable.icon_glass),
        SimpleListItem("Металл", imgRes = R.drawable.icon_metal),
        SimpleListItem("Батарейки", imgRes = R.drawable.icon_battery)
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleHolder =
        SimpleHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_simple_list, parent, false)
        )

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: SimpleHolder, position: Int) {
        holder.bind(list[position])
    }

    inner class SimpleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SimpleListItem) {
            itemView.item_title.text = item.title
            requestManager?.load(item.imgUrl)?.into(itemView.item_icon)
                ?: itemView.item_icon.setImageResource(item.imgRes)
            if (item.news != null)
            itemView.setOnClickListener {
                itemView.findNavController().navigate(R.id.actionOpenTopic, Bundle().apply {
                    putString(Constants.NEWS_KEY, Gson().toJson(item.news))
                })
            }
        }
    }

}