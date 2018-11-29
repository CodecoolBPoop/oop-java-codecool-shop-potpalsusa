CREATE TABLE "Product" (
	"id" serial NOT NULL,
	"name" varchar NOT NULL,
	"defaultPrice" float4 NOT NULL,
	"productCategoryId" int NOT NULL UNIQUE,
	"supplierId" int NOT NULL UNIQUE,
	"defaultCurrency" varchar NOT NULL DEFAULT 'USD',
	CONSTRAINT Product_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "ProductCategory" (
	"id" serial NOT NULL,
	"department" varchar NOT NULL,
	"description" varchar NOT NULL,
	"name" varchar NOT NULL,
	CONSTRAINT ProductCategory_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);



CREATE TABLE "Supplier" (
	"id" serial NOT NULL,
	"name" varchar NOT NULL,
	"description" varchar NOT NULL,
	CONSTRAINT Supplier_pk PRIMARY KEY ("id")
) WITH (
  OIDS=FALSE
);




ALTER TABLE product_category ADD CONSTRAINT "ProductCategory_fk0" FOREIGN KEY ("id") REFERENCES product(product_category_id);

ALTER TABLE supplier ADD CONSTRAINT "Supplier_fk0" FOREIGN KEY ("id") REFERENCES product(supplier_id);

