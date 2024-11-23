https://github.com/ffernandoss/taller4.git

# Proyecto Taller 4

## Paquetes y Clases

### `com.example.taller4`

- **`MainActivity`**: Actividad principal que muestra un saludo personalizado basado en la hora del día y permite la navegación a `SegundaVentana`.

- **`SegundaVentana`**: Actividad que utiliza un sensor de acelerómetro para cambiar el color de fondo y muestra un formulario para añadir y borrar coches.

- **`CarWidgetProvider`**: Proveedor de widget que muestra información de coches desde Firebase Firestore y permite alternar entre mostrar modelos y marcas.

- **`Car`**: Clase de datos que representa un coche con atributos como marca, color, modelo, matrícula y fecha de compra.

- **`CarAdapter`**: Adaptador de RecyclerView para mostrar una lista de coches.

- **`CarViewHolder`**: ViewHolder para `CarAdapter` que contiene vistas para mostrar la información de un coche.

- **`CarDetailsFragment`**: Fragmento que muestra los detalles de un coche seleccionado.

- **`CarListFragment`**: Fragmento que muestra una lista de coches y escucha actualizaciones en tiempo real desde Firebase Firestore.

## XML Layouts

### `res/layout`

- **`activity_main.xml`**: Layout para `MainActivity` que incluye un saludo, un botón de navegación y un contenedor de fragmentos.

- **`activity_segunda_ventana.xml`**: Layout para `SegundaVentana` que incluye contenedores de fragmentos y una vista Compose.

- **`fragment_car_list.xml`**: Layout para `CarListFragment` que contiene un RecyclerView para mostrar la lista de coches.

- **`fragment_car_details.xml`**: Layout para `CarDetailsFragment` que muestra los detalles de un coche.

- **`item_car.xml`**: Layout para cada elemento de la lista de coches en `CarAdapter`.

- **`car_widget.xml`**: Layout para el widget que muestra información de coches.

- **`car_widget_preview.xml`**: Layout de vista previa para el widget de coches.

### `res/xml`

- **`car_widget_info.xml`**: Configuración del widget que define sus propiedades como tamaño, actualización y diseño inicial.

- **`data_extraction_rules.xml`**: Reglas de extracción de datos para copias de seguridad y transferencias de dispositivos.
