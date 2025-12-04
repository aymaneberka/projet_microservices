import { Routes } from '@angular/router';
import { CatalogComponent } from './catalog/catalog.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { ManageProductsComponent } from './manage-products/manage-products.component';

export const routes: Routes = [
  { path: '', component: CatalogComponent },
  { path: 'manage-products', component: ManageProductsComponent },
  { path: 'products/:id', component: ProductDetailComponent },
  { path: '**', redirectTo: '' }
];
