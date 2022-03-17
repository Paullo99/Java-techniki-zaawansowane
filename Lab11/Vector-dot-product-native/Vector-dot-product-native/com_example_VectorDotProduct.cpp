#include "com_example_VectorDotProduct.h"
#include <iostream>
#include <thread>
#include <chrono>

using namespace std;

double count_vector_dot_product(JNIEnv* env, int array_size, jobjectArray a, jobjectArray b, jclass double_class) {

	double result = 0.0;

	double a_value, b_value;

	jobject a_element, b_element;

	//Metoda zwracaj¹ca wartoœæ obiektu Double
	jmethodID double_value = env->GetMethodID(double_class, "doubleValue", "()D");

	for (int i = 0; i < array_size; i++)
	{
		a_element = env->GetObjectArrayElement(a, i);
		b_element = env->GetObjectArrayElement(b, i);

		a_value = env->CallDoubleMethod(a_element, double_value);
		b_value = env->CallDoubleMethod(b_element, double_value);

		result += a_value * b_value;
	}

	return result;
}

JNIEXPORT jobject JNICALL Java_com_example_VectorDotProduct_multi01
(JNIEnv* env, jobject, jobjectArray a, jobjectArray b) {
	jsize a_length = env->GetArrayLength(a);
	jsize b_length = env->GetArrayLength(b);

	if (a_length != b_length)
	{
		printf("Podane tablice maj¹ ró¿ne rozmiary!\n");
		//https://stackoverflow.com/questions/23085044/jni-system-out-and-printf-behaviour
		fflush(stdout);
		return NULL;
	}
	else
	{
		//Znalezienie klasy Double
		jclass double_class = env->FindClass("java/lang/Double");

		double result = count_vector_dot_product(env, a_length, a, b, double_class);

		//Konstruktor dla klasy Double
		jmethodID initDouble = env->GetMethodID(double_class, "<init>", "(D)V");

		jobject return_object = env->NewObject(double_class, initDouble, result);

		env->DeleteLocalRef(double_class);

		return return_object;
	}
	
}

JNIEXPORT jobject JNICALL Java_com_example_VectorDotProduct_multi02
(JNIEnv* env, jobject this_obj, jobjectArray a) {
	jsize a_length = env->GetArrayLength(a);

	//Znalezenie klasy obiektu, na którym wywo³ywana jest metoda multi02
	jclass vector_dot_product_class = env->GetObjectClass(this_obj);

	//Znalezenie id pola b w tej klasie
	jfieldID b_id = env->GetFieldID(vector_dot_product_class, "b", "[Ljava/lang/Double;");

	//Wy³uskanie pola b i przypisanie do zmiennej
	jobjectArray b = (jobjectArray)env->GetObjectField(this_obj, b_id);

	jsize b_length = env->GetArrayLength(b);

	if (a_length != b_length)
	{
		printf("Podane tablice maj¹ ró¿ne rozmiary!\n");
		fflush(stdout);
		return NULL;
	}
	else
	{
		//Znalezienie klasy Double
		jclass double_class = env->FindClass("java/lang/Double");

		double result = count_vector_dot_product(env, a_length, a, b, double_class);

		//Konstruktor dla klasy Double
		jmethodID init_double = env->GetMethodID(double_class, "<init>", "(D)V");

		jobject return_object = env->NewObject(double_class, init_double, result);

		env->DeleteLocalRef(double_class);
		env->DeleteLocalRef(b);
		env->DeleteLocalRef(vector_dot_product_class);

		return return_object;
	}
}

JNIEXPORT void JNICALL Java_com_example_VectorDotProduct_multi03
(JNIEnv* env, jobject this_obj) {

	//Znalezienie klasy MyJDialog + stworzenie obiektu
	jclass jdialog_class = env->FindClass("Lcom/example/MyJDialog;");
	jmethodID init_jdialog = env->GetMethodID(jdialog_class, "<init>", "()V");
	jobject my_jdialog_object =  env->NewObject(jdialog_class, init_jdialog);

	jmethodID get_is_btn_clicked = env->GetMethodID(jdialog_class, "getIsBtnClicked", "()Z");

	while (env->CallBooleanMethod(my_jdialog_object, get_is_btn_clicked) == JNI_FALSE)
		this_thread::sleep_for(chrono::milliseconds(100));

	//Wywo³anie funkcji zamykaj¹cej okienko
	jmethodID dispose_id = env->GetMethodID(jdialog_class, "dispose", "()V");
	env->CallVoidMethod(my_jdialog_object, dispose_id);

	//Pobranie wektorów wpisanych przez u¿ytkownika
	jmethodID get_vector_a = env->GetMethodID(jdialog_class, "getVectorA", "()[Ljava/lang/Double;");
	jobjectArray vector_a = (jobjectArray)env->CallObjectMethod(my_jdialog_object, get_vector_a);

	jmethodID get_vector_b = env->GetMethodID(jdialog_class, "getVectorB", "()[Ljava/lang/Double;");
	jobjectArray vector_b = (jobjectArray)env->CallObjectMethod(my_jdialog_object, get_vector_b);

	//Znalezenie klasy obiektu, na którym wywo³ywana jest metoda multi02
	jclass vector_dot_product_class = env->GetObjectClass(this_obj);

	//Znalezenie id pola a,b oraz c w tej klasie
	jfieldID a_id = env->GetFieldID(vector_dot_product_class, "a", "[Ljava/lang/Double;");
	jfieldID b_id = env->GetFieldID(vector_dot_product_class, "b", "[Ljava/lang/Double;");
	jfieldID c_id = env->GetFieldID(vector_dot_product_class, "c", "Ljava/lang/Double;");

	//Ustawienie wartoœci wektorów A i B
	env->SetObjectField(this_obj, a_id, vector_a);
	env->SetObjectField(this_obj, b_id, vector_b);

	if (env->GetArrayLength(vector_a) != env->GetArrayLength(vector_b))
	{
		printf("Podane tablice maj¹ ró¿ne rozmiary!\n");
		fflush(stdout);
		return;
	}
	else
	{
		jmethodID multi04 = env->GetMethodID(vector_dot_product_class, "multi04", "()V");
		env->CallVoidMethod(this_obj, multi04);

		jobject c = env->GetObjectField(this_obj, c_id);

		jclass double_class = env->FindClass("java/lang/Double");
		jmethodID double_value = env->GetMethodID(double_class, "doubleValue", "()D");
		jdouble result = env->CallDoubleMethod(c, double_value);

		env->DeleteLocalRef(c);
		env->DeleteLocalRef(double_class);
	}

	env->DeleteLocalRef(jdialog_class);
	env->DeleteLocalRef(my_jdialog_object);
	env->DeleteLocalRef(vector_a);
	env->DeleteLocalRef(vector_b);

}