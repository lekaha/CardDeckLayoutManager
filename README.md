# Card Deck LayoutManager for RecyclerView

[ ![Download](https://api.bintray.com/packages/lekaha/MavenRepo/com.github.lekaha.carddecklayoutmanager/images/download.svg?version=1.0.0) ](https://bintray.com/lekaha/MavenRepo/com.github.lekaha.carddecklayoutmanager/1.0.0/link) [![CircleCI](https://circleci.com/gh/lekaha/CardDeckLayoutManager/tree/master.svg?style=svg)](https://circleci.com/gh/lekaha/CardDeckLayoutManager/tree/master)

Scroll RecyclerView as scrolling a deck of cards. This library offers a custom LayoutManager to achieve it.

### Add to your dependencies

```groovy
dependencies {
    implementation "com.github.lekaha:carddecklayoutmanager:1.0.0"
}
```

### How to use

```kotlin
val recycler = findViewById<RecyclerView>(R.id.recycler)

// revealHeight = that interval space between cards 
recycler.layoutManager = CardDeckLayoutManager(this, recycler, revealHeight)
```

### Optional

If you would like to make your items in the list are more like `card` you can find this [Drawable resource file](/app/src/main/res/drawable/bg_card.xml)
And add it as your item layout background. 
For instance,

```xml
<androidx.constraintlayout.widget.ConstraintLayout
        xmlns:android="http://schemas.android.com/apk/res/android"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="@drawable/bg_card"> <-- here
</androidx.constraintlayout.widget.ConstraintLayout>
```

![preview](/screens/item.png)

### Known issues

- Not yet have recycling mechanism

### Preview
![preview](/screens/anim_card.gif)
