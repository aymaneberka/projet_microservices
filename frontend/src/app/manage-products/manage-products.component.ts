import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule, FormBuilder, Validators } from '@angular/forms';
import { ProductService } from '../services/product.service';
import { Product } from '../models/product.model';

@Component({
  selector: 'app-manage-products',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './manage-products.component.html',
  styleUrl: './manage-products.component.scss'
})
export class ManageProductsComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly productService = inject(ProductService);

  form = this.fb.group({
    name: ['', Validators.required],
    description: [''],
    price: [0, [Validators.required, Validators.min(0)]],
    stock: [0, [Validators.required, Validators.min(0)]]
  });

  products: Product[] = [];
  loading = true;
  submitting = false;
  error?: string;
  info?: string;
  selected?: Product;

  ngOnInit(): void {
    this.fetchProducts();
  }

  fetchProducts(): void {
    this.loading = true;
    this.productService.getProducts().subscribe({
      next: (list: Product[]) => {
        this.products = list;
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossible de charger les produits.';
        this.loading = false;
      }
    });
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }
    this.submitting = true;
    this.error = undefined;
    this.info = undefined;

    const payload = {
      name: this.form.value.name || '',
      description: this.form.value.description || '',
      price: Number(this.form.value.price ?? 0),
      stock: Number(this.form.value.stock ?? 0)
    };

    const obs = this.selected
      ? this.productService.updateProduct(this.selected.id, payload)
      : this.productService.createProduct(payload);

    obs.subscribe({
      next: (product: Product) => {
        this.info = this.selected
          ? `Produit "${product.name}" mis a jour.`
          : `Produit "${product.name}" cree.`;
        this.form.reset({ price: 0, stock: 0 });
        this.selected = undefined;
        this.fetchProducts();
        this.submitting = false;
      },
      error: () => {
        this.error = 'Echec lors de la sauvegarde du produit.';
        this.submitting = false;
      }
    });
  }

  edit(product: Product): void {
    this.selected = product;
    this.form.patchValue({
      name: product.name,
      description: product.description,
      price: product.price,
      stock: product.stock
    });
  }

  delete(product: Product): void {
    if (!confirm(`Supprimer "${product.name}" ?`)) {
      return;
    }
    this.submitting = true;
    this.productService.deleteProduct(product.id).subscribe({
      next: () => {
        this.info = `Produit "${product.name}" supprime.`;
        this.selected = undefined;
        this.fetchProducts();
        this.submitting = false;
      },
      error: () => {
        this.error = 'Echec lors de la suppression.';
        this.submitting = false;
      }
    });
  }

  cancelEdit(): void {
    this.selected = undefined;
    this.form.reset({ price: 0, stock: 0 });
  }
}
