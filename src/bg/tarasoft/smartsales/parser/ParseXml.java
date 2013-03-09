package bg.tarasoft.smartsales.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import bg.tarasoft.smartsales.bean.Category;
import bg.tarasoft.smartsales.bean.Model;
import bg.tarasoft.smartsales.bean.Product;
import bg.tarasoft.smartsales.bean.Serie;
import bg.tarasoft.smartsales.bean.Store;
import bg.tarasoft.smartsales.bean.StoreType;
import bg.tarasoft.smartsales.database.SeriesDataSource;
import bg.tarasoft.smartsales.database.SeriesModelsDataSource;
import bg.tarasoft.smartsales.database.SeriesProductsDataSource;

public class ParseXml {

	private static final String MODEL = "model";
	private static final String PRODUCT = "product";
	private static final String CHECKSUM = "checksum";
	private static final String CATEGORY_ID = "category_id";
	private static final String CATEGORY = "category";
	private static final String ID = "id";
	private static final String PARENT_ID = "parent_id";
	private static final String NAME = "name";
	private static final String PIC = "pic";
	private static final String SORTORDER = "sortorder";
	private static final String STORE = "store";
	private static final String STORE_CONTR = "retail_contr";
	private static final String STORE_NUMBER = "store_number";
	private static final String PRODUCT_PRICE = "price";
	private static final String MODEL_ID = "model_id";

	private static String ITEM = "item";

	public static List<Serie> parseSeries(Document doc, List<Serie> series) {
		try {

			NodeList listOfCategories = doc.getElementsByTagName(ITEM);

			Serie serie = null;

			for (int s = 0; s < listOfCategories.getLength(); s++) {
				Node firstPersonNode = listOfCategories.item(s);
				if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstPersonElement = (Element) firstPersonNode;
					serie = new Serie();
					serie.setId(Integer.valueOf(getElementByName(
							firstPersonElement, ID)));
					serie.setName(getElementByName(firstPersonElement, NAME));
					System.out.println("NAME: " + serie.getName());

					serie.setPicUrl(getElementByName(firstPersonElement, PIC));
					series.add(serie);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return series;
	}

	public static List<Category> parseCategories(Document doc,
			List<Category> categories, Context context) {

		SeriesDataSource seriesDS = new SeriesDataSource(context);
		seriesDS.open();
		seriesDS.deleteRowsIfTableExists();

		try {

			NodeList listOfCategories = doc.getElementsByTagName(CATEGORY);

			Category category = null;

			List<Serie> seriesList = null;

			for (int s = 0; s < listOfCategories.getLength(); s++) {
				Node firstPersonNode = listOfCategories.item(s);
				if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstPersonElement = (Element) firstPersonNode;
					category = new Category();
					category.setId(Integer.valueOf(getElementByName(
							firstPersonElement, ID)));
					category.setParentId(Integer.valueOf(getElementByName(
							firstPersonElement, PARENT_ID)));
					category.setName(getElementByName(firstPersonElement, NAME));
					category.setImageUrl(getElementByName(firstPersonElement,
							PIC));
					category.setSortOrder(Integer.valueOf(getElementByName(
							firstPersonElement, SORTORDER)));

					NodeList series = firstPersonElement
							.getElementsByTagName("series");

					if (series != null) {
						Node n = series.item(0);
						if (n != null) {
							seriesList = new ArrayList<Serie>();
							System.out.println(n.getTextContent());
							NodeList list = n.getChildNodes();
							System.out.println("Length: " + list.getLength());
							Serie serie = null;
							for (int i = 0; i < list.getLength(); i++) {
								if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
									serie = new Serie();
									serie.setId(Integer.valueOf(list.item(i)
											.getTextContent()));
									serie.setCategoryId(category.getId());

									seriesList.add(serie);
								}
							}
							System.out.println(seriesList);
							seriesDS.insertSeries(seriesList);
						}
					}

					categories.add(category);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		seriesDS.close();

		return categories;
	}

	public static List<Product> parseProducts(Document doc,
			List<Product> products, Context context, ProgressDialog progress) {
		SeriesProductsDataSource dataSource = new SeriesProductsDataSource(
				context);
		dataSource.open();
		dataSource.deleteRowsIfTableExists();
		try {

			NodeList listOfCategories = doc.getElementsByTagName(PRODUCT);

			Product product = null;

			List<Integer> seriesList = null;

			HashMap<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();

			for (int s = 0; s < listOfCategories.getLength(); s++) {
				Node firstPersonNode = listOfCategories.item(s);
				if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstPersonElement = (Element) firstPersonNode;
					product = new Product();
					product.setId(Integer.valueOf(getElementByName(
							firstPersonElement, ID)));
					product.setCategoryId(Integer.valueOf(getElementByName(
							firstPersonElement, CATEGORY_ID)));
					product.setPrice(Integer.valueOf(getElementByName(
							firstPersonElement, PRODUCT_PRICE)));
					product.setName(getElementByName(firstPersonElement, NAME));
					product.setImageUrl(getElementByName(firstPersonElement,
							PIC));
					product.setModelId(Integer.valueOf(getElementByName(
							firstPersonElement, MODEL_ID)));
					// SETING LABEL
					setProductStatus(product, firstPersonElement);
					NodeList series = firstPersonElement
							.getElementsByTagName("series");

					if (series != null) {
						Node n = series.item(0);
						if (n != null) {
							seriesList = new ArrayList<Integer>();
							System.out.println(n.getTextContent());
							NodeList list = n.getChildNodes();
							System.out.println("Length: " + list.getLength());
							// Serie serie = null;

							for (int i = 0; i < list.getLength(); i++) {
								if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
									seriesList.add(Integer.valueOf(list.item(i)
											.getTextContent()));

								}
							}
							System.out.println("IDIDID: " + product.getId()
									+ "LISTLIST: " + seriesList);

							map.put(product.getId(), seriesList);

							// dataSource.insertValues(seriesList,
							// product.getId(), progress);
						}
					}

					products.add(product);
				}
			}
			dataSource.insertValues(map, progress);
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("RETURN PROD");
		dataSource.close();

		return products;
	}

	private static void setProductStatus(Product product,
			Element firstPersonElement) {
		String status = getElementByName(firstPersonElement, "status");
		if (status.equals("new")) {
			product.setLabel(Product.LABEL_NEW);
			Log.d("asd", "Setting STATUS: NEW");
		} else if (status.equals("last")) {
			product.setLabel(Product.LABEL_LAST);
			Log.d("asd", "Setting STATUS: LAST");
		} else if (status.equals("promo")) {
			product.setLabel(Product.LABEL_PROMO);
			Log.d("asd", "Setting STATUS: PROMO");
		} else {
			product.setLabel(Product.LABEL_NONE);
			Log.d("asd", "Setting STATUS: NONE");
		}
	}

	public static String parseChecksum(Document doc) {
		System.out.println(doc.getDocumentElement().getChildNodes().item(0)
				.getNodeValue());

		return doc.getDocumentElement().getChildNodes().item(0).getNodeValue();

	}

	private static String getElementByName(Element rootElement, String name) {
		NodeList firstNameList = rootElement.getElementsByTagName(name);
		Element firstNameElement = (Element) firstNameList.item(0);

		NodeList textFNList = firstNameElement.getChildNodes();
		String value = "";
		try {
			value = new String(((Node) textFNList.item(0)).getNodeValue()
					.trim());
		} catch (Exception e) {
			value = "";
		}
		return value;
	}

	public static void parseStores(Document doc, List<StoreType> storeRetails,
			Context context) {

		try {

			NodeList listOfRetails = doc.getElementsByTagName(STORE_CONTR);

			Store store = null;
			List<Store> storeList = null;
			StoreType storeRetail = null;

			for (int s = 0; s < listOfRetails.getLength(); s++) {
				Node firstPersonNode = listOfRetails.item(s);
				if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstPersonElement = (Element) firstPersonNode;

					storeRetail = new StoreType();
					storeRetail.setId(Integer.valueOf(getElementByName(
							firstPersonElement, ID)));
					storeRetail.setName(String.valueOf(getElementByName(
							firstPersonElement, NAME)));

					NodeList nodesList = firstPersonElement.getChildNodes();
					for (int i = 0; i < nodesList.getLength(); i++) {
						Node n = nodesList.item(i);
						if (n.getNodeName().equals("stores")) {
							NodeList childNodes = n.getChildNodes();
							storeList = new ArrayList<Store>();
							for (int j = 0; j < childNodes.getLength(); j++) {
								Node child = childNodes.item(j);
								if (child.getNodeType() == Node.ELEMENT_NODE) {
									Element storeElement = (Element) child;
									store = new Store();
									store.setStoreID(Integer
											.valueOf(getElementByName(
													storeElement, ID)));
									store.setStoreName(String
											.valueOf(getElementByName(
													storeElement, NAME)));
									// store.setHallId(Integer
									// .valueOf(getElementByName(
									// storeElement, STORE_NUMBER)));

									storeList.add(store);
								}
							}
							storeRetail.setStores(storeList);
						}
					}
				}
				storeRetails.add(storeRetail);
			}
			for (StoreType el : storeRetails) {
				System.out.println("RETAIL: " + el.getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void parseModels(Context context, Document doc,
			List<Model> models, ProgressDialog progress) {

		SeriesModelsDataSource dataSource = new SeriesModelsDataSource(context);
		dataSource.open();
		dataSource.deleteRowsIfTableExists();

		try {
			NodeList listOfCategories = doc.getElementsByTagName(MODEL);

			Model model = null;

			List<Integer> seriesList = null;

			HashMap<Integer, List<Integer>> map = new HashMap<Integer, List<Integer>>();

			for (int s = 0; s < listOfCategories.getLength(); s++) {
				Node firstPersonNode = listOfCategories.item(s);
				if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstPersonElement = (Element) firstPersonNode;
					model = new Model();
					model.setId(Integer.valueOf(getElementByName(
							firstPersonElement, ID)));
					model.setModelName(getElementByName(firstPersonElement,
							NAME));
					model.setModelPicUrl(getElementByName(firstPersonElement,
							PIC));
					NodeList series = firstPersonElement
							.getElementsByTagName("series");

					if (series != null) {
						Node n = series.item(0);
						if (n != null) {
							seriesList = new ArrayList<Integer>();
							NodeList list = n.getChildNodes();

							for (int i = 0; i < list.getLength(); i++) {
								if (list.item(i).getNodeType() == Node.ELEMENT_NODE) {
									seriesList.add(Integer.valueOf(list.item(i)
											.getTextContent()));

								}
							}

							map.put(model.getId(), seriesList);

						}
					}

					models.add(model);
				}
			}
			dataSource.insertValues(map, progress);
		} catch (Exception e) {
			e.printStackTrace();
		}

		dataSource.close();
	}

	public static void parseUser(Document doc, Context context) {
		SharedPreferences preferences = context.getSharedPreferences(
				"settings", 0);
		SharedPreferences.Editor edit = preferences.edit();
		try {
			NodeList childNodes = doc.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node firstPersonNode = childNodes.item(i);
				if (firstPersonNode.getNodeType() == Node.ELEMENT_NODE) {
					Element firstPersonElement = (Element) firstPersonNode;
					String admin = getElementByName(firstPersonElement,
							"admin_rights");
					if ("n".equals(admin)) {
						edit.putBoolean("admin_rights", false);
					} else {
						edit.putBoolean("admin_rights", true);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		edit.apply();
	}
}
