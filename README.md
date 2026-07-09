# ShapeView

一个 Android XML Shape 控件库，可以直接在布局中配置圆角、实心色、渐变、状态色、描边、虚线、图片圆角裁剪和阴影，减少手写 `drawable/selector/shape` XML。

## 特性

- 支持矩形、圆角矩形、椭圆、圆形、线条、圆环。
- 支持默认、按下、选中、禁用、焦点等状态色。
- 支持实心渐变、描边、虚线描边、描边渐变。
- 支持自绘阴影、彩色阴影、阴影偏移。
- `ShapeImageView` 支持图片圆角裁剪、圆形裁剪、图片外圈边框。
- `ShapeTextView` / `ShapeEditText` 会处理主题默认 padding，并支持 `android:paddingHorizontal` / `android:paddingVertical`。

## 安装

项目已配置 JitPack 发布脚本。发布到 GitHub 并创建 tag 后，在使用方项目中添加：

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}
```

```kotlin
// build.gradle.kts
dependencies {
    implementation("com.github.gegeName:shapeview:0.0.2")
}
```

如果仓库是 `gegeName/shapeview`，通常坐标为：

```kotlin
implementation("com.github.gegeName:shapeview:0.0.2")
```

## 支持控件

| 控件 | 说明 |
| --- | --- |
| `ShapeView` | 普通 View，适合作为背景块、线条、圆环 |
| `ShapeTextView` | 支持 shape 属性的 TextView |
| `ShapeButton` | 支持 shape 属性的 Button |
| `ShapeImageView` | 支持图片圆角/圆形裁剪和图片边框 |
| `ShapeEditText` | 支持 shape 属性的 EditText |
| `ShapeLinearLayout` | 支持 shape 属性的 LinearLayout |
| `ShapeFrameLayout` | 支持 shape 属性的 FrameLayout |
| `ShapeConstraintLayout` | 支持 shape 属性的 ConstraintLayout |
| `ShapeRecyclerView` | 支持 shape 属性的 RecyclerView |

## 快速使用

先在布局根节点声明命名空间：

```xml
xmlns:app="http://schemas.android.com/apk/res-auto"
```

### 圆角背景

```xml
<com.chat.shapeview.ShapeTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:paddingHorizontal="16dp"
    android:paddingVertical="10dp"
    android:text="圆角背景"
    android:textColor="#FFFFFF"
    app:shape_radius_L="8dp"
    app:shape_solidColor_L="#3F51B5" />
```

### 状态色按钮

```xml
<com.chat.shapeview.ShapeButton
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:text="按一下"
    android:textColor="#FFFFFF"
    app:shape_radius_L="24dp"
    app:shape_solidColor_L="#2196F3"
    app:shape_solidPressedColor_L="#FFC107"
    app:shape_solidSelectedColor_L="#3ABF3F"
    app:shape_solidDisabledColor_L="#1F1E1E" />
```

如果要点击后切换选中态，需要自己切换 `selected`：

```kotlin
button.setOnClickListener {
    it.isSelected = !it.isSelected
}
```

### 渐变背景

```xml
<com.chat.shapeview.ShapeTextView
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:gravity="center"
    android:padding="16dp"
    android:text="线性渐变"
    android:textColor="#FFFFFF"
    app:shape_radius_L="8dp"
    app:shape_solidGradientStartColor_L="#F44336"
    app:shape_solidGradientCenterColor_L="#9C27B0"
    app:shape_solidGradientEndColor_L="#03A9F4"
    app:shape_solidGradientOrientation_L="leftToRight"
    app:shape_solidGradientType_L="linear" />
```

### 图片圆角、圆形和边框

`ShapeImageView` 会裁剪图片本身。设置边框后，图片区域会向内缩，边框围绕图片外圈绘制，不会覆盖图片内容。

```xml
<com.chat.shapeview.ShapeImageView
    android:layout_width="80dp"
    android:layout_height="80dp"
    android:scaleType="centerCrop"
    android:src="@drawable/avatar"
    app:shape_radius_L="16dp"
    app:shape_strokeColor_L="#E91E63"
    app:shape_strokeSize_L="3dp" />
```

```xml
<com.chat.shapeview.ShapeImageView
    android:layout_width="80dp"
    android:layout_height="80dp"
    android:scaleType="centerCrop"
    android:src="@drawable/avatar"
    app:shape_circle_L="true"
    app:shape_strokeColor_L="#2E7D32"
    app:shape_strokeDashSize_L="8dp"
    app:shape_strokeDashGap_L="4dp"
    app:shape_strokeSize_L="3dp" />
```

### 阴影

```xml
<com.chat.shapeview.ShapeTextView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:padding="16dp"
    android:text="彩色阴影"
    app:shape_radius_L="8dp"
    app:shape_solidColor_L="#FFFFFF"
    app:shape_shadowElevation_L="8dp"
    app:shape_shadowSpotColor_L="#FF4081" />
```

## 属性说明

### 基础形状

| 属性 | 类型 | 说明 |
| --- | --- | --- |
| `shape_L` | enum | 形状类型：`none`、`rectangle`、`oval`、`line`、`ring` |
| `shape_circle_L` | boolean | 强制圆形。对 `ShapeImageView` 会按短边裁成正圆 |
| `shape_width_L` | dimension | Drawable 显式宽度 |
| `shape_height_L` | dimension | Drawable 显式高度 |

### 实心填充和状态色

| 属性 | 类型 | 说明 |
| --- | --- | --- |
| `shape_solidColor_L` | color | 默认填充色 |
| `shape_solidPressedColor_L` | color | 按下状态填充色 |
| `shape_solidCheckedColor_L` | color | checked 状态填充色 |
| `shape_solidDisabledColor_L` | color | 禁用状态填充色 |
| `shape_solidFocusedColor_L` | color | 焦点状态填充色 |
| `shape_solidSelectedColor_L` | color | selected 状态填充色 |

### 实心渐变

| 属性 | 类型 | 说明 |
| --- | --- | --- |
| `shape_solidGradientStartColor_L` | color | 渐变起始色 |
| `shape_solidGradientCenterColor_L` | color | 渐变中间色，可选 |
| `shape_solidGradientEndColor_L` | color | 渐变结束色 |
| `shape_solidGradientOrientation_L` | enum | 渐变方向 |
| `shape_solidGradientType_L` | enum | `linear`、`radial`、`sweep` |
| `shape_solidGradientRadius_L` | dimension | 径向渐变半径 |
| `shape_solidGradientCenterX_L` | float | 渐变中心 X，默认 `0.5` |
| `shape_solidGradientCenterY_L` | float | 渐变中心 Y，默认 `0.5` |
| `shape_solidGradientUseLevel_L` | boolean | 是否随 level 变化 |

渐变方向可选：

| 值 | 方向 |
| --- | --- |
| `leftToRight` | 左到右 |
| `topLeftToBottomRight` | 左上到右下 |
| `topToBottom` | 上到下 |
| `topRightToBottomLeft` | 右上到左下 |
| `rightToLeft` | 右到左 |
| `bottomRightToTopLeft` | 右下到左上 |
| `bottomToTop` | 下到上 |
| `bottomLeftToTopRight` | 左下到右上 |

### 圆角

| 属性 | 类型 | 说明 |
| --- | --- | --- |
| `shape_radius_L` | dimension | 统一圆角半径 |
| `shape_topLeftRadius_L` | dimension | 左上角圆角 |
| `shape_topRightRadius_L` | dimension | 右上角圆角 |
| `shape_bottomLeftRadius_L` | dimension | 左下角圆角 |
| `shape_bottomRightRadius_L` | dimension | 右下角圆角 |

### 描边

| 属性 | 类型 | 说明 |
| --- | --- | --- |
| `shape_strokeColor_L` | color | 默认描边色 |
| `shape_strokePressedColor_L` | color | 按下状态描边色 |
| `shape_strokeCheckedColor_L` | color | checked 状态描边色 |
| `shape_strokeDisabledColor_L` | color | 禁用状态描边色 |
| `shape_strokeFocusedColor_L` | color | 焦点状态描边色 |
| `shape_strokeSelectedColor_L` | color | selected 状态描边色 |
| `shape_strokeSize_L` | dimension | 描边宽度 |
| `shape_strokeDashSize_L` | dimension | 虚线段长度 |
| `shape_strokeDashGap_L` | dimension | 虚线间隔 |

### 描边渐变

| 属性 | 类型 | 说明 |
| --- | --- | --- |
| `shape_strokeGradientStartColor_L` | color | 描边渐变起始色 |
| `shape_strokeGradientCenterColor_L` | color | 描边渐变中间色，可选 |
| `shape_strokeGradientEndColor_L` | color | 描边渐变结束色 |
| `shape_strokeGradientOrientation_L` | enum | 描边渐变方向，取值同实心渐变方向 |

说明：普通背景受 `GradientDrawable` 限制，描边渐变会退化为起始色；`ShapeImageView` 的图片边框由控件自绘，支持真正的线性渐变描边。

### Ring 圆环

仅在 `shape_L="ring"` 时使用。

| 属性 | 类型 | 说明 |
| --- | --- | --- |
| `shape_innerRadius_L` | dimension | 圆环内径 |
| `shape_innerRadiusRatio_L` | float | 圆环内径比例 |
| `shape_thickness_L` | dimension | 圆环厚度 |
| `shape_thicknessRatio_L` | float | 圆环厚度比例 |
| `shape_useLevel_L` | boolean | 是否随 level 变化 |

### Line 线条

仅在 `shape_L="line"` 时使用。

| 属性 | 类型 | 说明 |
| --- | --- | --- |
| `shape_lineGravity_L` | flags | 线条位置：`top`、`bottom`、`center_vertical`、`left`、`right`、`center_horizontal`、`center` |

### 阴影

| 属性 | 类型 | 说明 |
| --- | --- | --- |
| `shape_shadowElevation_L` | dimension | 阴影高度 |
| `shape_shadowMaxElevation_L` | dimension | 最大阴影高度，兼容预留字段 |
| `shape_shadowUseCompatPadding_L` | boolean | 是否启用兼容 padding，兼容预留字段 |
| `shape_shadowPreventCornerOverlap_L` | boolean | 是否防止圆角内容重叠，兼容预留字段 |
| `shape_shadowSpotColor_L` | color | 阴影主光源颜色 |
| `shape_shadowAmbientColor_L` | color | 阴影环境光颜色 |
| `shape_shadowAlpha_L` | float | 阴影整体透明度，范围 `0.0` 到 `1.0` |
| `shape_shadowOffsetX_L` | dimension | 阴影水平偏移 |
| `shape_shadowOffsetY_L` | dimension | 阴影垂直偏移 |

## ShapeImageView 支持说明

`ShapeImageView` 除了设置背景，还会处理图片内容：

- `shape_radius_L` 和四个单角圆角会裁剪图片。
- `shape_L="oval"` 会按控件区域裁成椭圆。
- `shape_circle_L="true"` 会按短边裁成正圆。
- `shape_stroke*` 会绘制在图片外圈，图片区域会按描边宽度自动内缩。
- `shape_strokeDash*` 和 `shape_strokeGradient*` 支持图片边框。

## 注意事项

- 状态色依赖 View 的真实状态。比如 `shape_solidSelectedColor_L` 需要设置 `view.isSelected = true` 才会显示。
- `ShapeTextView` / `ShapeEditText` 默认会清掉主题带来的 padding；XML 中显式设置 `android:padding`、`paddingHorizontal`、`paddingVertical`、`paddingStart` 等会被保留。
- 阴影使用自绘实现，父布局会自动关闭 `clipChildren` / `clipToPadding`，以避免阴影被裁剪。
- 如果设置了虚线描边，控件会启用软件层以保证虚线绘制稳定。

## License

Apache License 2.0

## 许可证

本项目基于 [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0) 开源。Apache 2.0 自带"按现状提供、不作担保、不承担责任"的条款，并额外包含专利授权与责任限制条款。

## 免责声明 / Disclaimer

本项目（以下简称"本软件"）是一个通用的媒体选择工具，仅供学习、研究和合法用途使用。

1. 本软件按"现状"提供，作者不对其适用性、可靠性、安全性作任何明示或暗示的担保。
2. 使用者应自行遵守所在国家/地区的法律法规。对于使用者利用本软件从事的任何违法、侵权或其他不当行为，作者不承担由此产生的任何责任。
3. 本软件不针对任何违法用途设计，作者不认可、不支持将其用于任何违反法律法规的用途。
4. 在适用法律允许的最大范围内，作者不对因使用或无法使用本软件而导致的任何直接或间接损失承担责任。
5. 使用本软件即表示使用者已知悉并接受以上条款。
