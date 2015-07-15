package ru.jader.xsdtool.common;

public class KeyValue<K, V> {
	   private K key;
	   private V value;
	 
	   public KeyValue(K key, V value) {
	      this.key = key;
		  this.value = value;
	   }
	 
	   public K getKey() {
	      return key;
	   }
	   
	   public V getValue() {
	      return value;
	   }
	   
	   public String toString() {
	      return key.toString();
	   }
}
