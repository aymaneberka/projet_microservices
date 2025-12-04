import { Component, OnInit, inject } from '@angular/core';
import { CommonModule, DecimalPipe } from '@angular/common';
import { ActivatedRoute, ParamMap, RouterLink } from '@angular/router';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { catchError, of, switchMap } from 'rxjs';
import { Product } from '../models/product.model';
import { ProductService } from '../services/product.service';
import { OrderService } from '../services/order.service';
import { CreateOrderResponse } from '../models/order.model';

@Component({
  selector: 'app-product-detail',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink, DecimalPipe],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
  private readonly fb = inject(FormBuilder);
  private readonly route = inject(ActivatedRoute);
  private readonly productService = inject(ProductService);
  private readonly orderService = inject(OrderService);

  product?: Product;
  loading = true;
  error?: string;
  orderResponse?: CreateOrderResponse;
  submitting = false;

  form = this.fb.group({
    quantity: [1, [Validators.required, Validators.min(1)]],
    paymentMethod: ['CARD', [Validators.required]],
    firstName: ['', [Validators.required]],
    lastName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]]
  });

  constructor() {}

  ngOnInit(): void {
    this.route.paramMap
      .pipe(
        switchMap((params: ParamMap) => {
          const id = Number(params.get('id'));
          return this.productService.getProduct(id).pipe(
            catchError(() => {
              this.error = 'Produit introuvable ou service indisponible.';
              this.loading = false;
              return of(undefined);
            })
          );
        })
      )
      .subscribe((product: Product | undefined) => {
        if (product) {
          this.product = product;
        }
        this.loading = false;
      });
  }

  get qty(): number {
    const value = this.form.get('quantity')?.value ?? 1;
    return Number.isFinite(value) && value > 0 ? value : 1;
  }

  get total(): number {
    return (this.product?.price ?? 0) * this.qty;
  }

  submit(): void {
    if (!this.product) {
      this.error = 'Produit introuvable';
      return;
    }

    this.orderResponse = undefined;
    this.error = undefined;

    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.submitting = true;

    const payload = {
      productId: this.product.id,
      quantity: this.qty,
      paymentMethod: this.form.value.paymentMethod || 'CARD',
      client: {
        firstName: this.form.value.firstName || '',
        lastName: this.form.value.lastName || '',
        email: this.form.value.email || ''
      }
    };

    this.orderService.createOrder(payload).subscribe({
      next: (res: CreateOrderResponse) => {
        this.orderResponse = res;
        // Ajuster le stock affiché côté client
        this.product = {
          ...this.product!,
          stock: Math.max(0, (this.product?.stock ?? 0) - this.qty)
        };
        this.submitting = false;
      },
      error: () => {
        this.error = 'Commande impossible pour le moment. Vérifiez les informations ou réessayez.';
        this.submitting = false;
      }
    });
  }
}
