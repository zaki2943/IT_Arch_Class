package com.example.it_arch_aidl

import android.util.Log
import java.math.BigDecimal
import java.util.Stack



class StringCalc {
    // オペレーターの優先順位
    private val opPriority: Map<String, Int> = hashMapOf(
        "+" to 2,
        "-" to 2,
        "*" to 3,
        "/" to 3
    )
    fun String.isNumber(): Boolean {
        val pattern = java.util.regex.Pattern.compile("^[0-9]*$")
        val matcher = pattern.matcher(this)
        return matcher.matches()
    }

    /**
     * 数式の文字列を与えると計算結果を返す
     * @param formula 数式の文字列。トークンは空白で区切られている必要がある
     * @return
     */
    fun calculate(formula: String): Double {
        val ops: Stack<String> = Stack() // オペレーターのスタック
        val vals: Stack<Double> = Stack() // 値のスタック
        val tokens = formula.split(" ".toRegex()).toTypedArray()
        Log.d("tag11","formula: $formula")
        Log.d("tag11","===============")
        for (token in tokens) {
            Log.d("tag11","token: $token")
            if (token.isNumber()) {
                // tokenが数値なら値スタックに積む
                vals.push(token.toDouble())
            } else if (isOperator(token)) {
                // tokenがオペレータ(o1)の時
                // - オペレータスタックのトップにオペレータo2 があり、o1 が左結合性で、
                //   かつ優先順位が o2 と等しいか低い場合、以下を繰り返す
                //     - o2 をオペレータスタックからpopし、値スタックから値を二つpopし、
                //       演算し、結果を値スタックにpushする
                // - o1 をオペレータスタックにプッシュする。
                // (右結合性のオペレータは未実装)
                while (ops.size > 0) {
                    val lastOp: String = ops.pop()
                    // "("が入っている場合があるため
                    // オペレータスタックの最後もオペレータであることを確認する
                    if (isOperator(lastOp) && getOpPriority(token) <= getOpPriority(lastOp)) {
                        val val2: Double = vals.pop()
                        val val1: Double = vals.pop()
                        vals.push(applyOperator(lastOp, val1, val2))
                    } else {
                        ops.push(lastOp)
                        break
                    }
                }
                ops.push(token)
            } else if (token == "(") {
                // トークンが左括弧の場合、オペレータスタックにプッシュする
                ops.push(token)
            } else if (token == ")") {
                // トークンが右括弧の場合
                // - オペレータスタックのトップにあるトークンが左括弧になるまで、
                //   オペレータスタックからオペレータをpopし、値スタックから値を二つpopし
                //   それらを演算し、値スタックに結果をプッシュする
                // - 左括弧をスタックからpopするが、何もせずに捨てる
                while (ops.size > 0) {
                    val op: String = ops.pop()
                    if (op == "(") {
                        break
                    } else {
                        val val2: Double = vals.pop()
                        val val1: Double = vals.pop()
                        vals.push(applyOperator(op, val1, val2))
                    }
                }
            }
            Log.d("tag11","ops: $ops")
            Log.d("tag11","vals: $vals")
            Log.d("tag11","------------")
        }

        // 読み取るべきトークンが無くなったら、オペレータスタックが空になるまで
        // オペレータスタックからオペレータをpopし、値スタックから値を二つpopし
        // それらを演算し、値スタックに結果をプッシュする
        while (ops.size > 0) {
            Log.d("tag11","last calculation")
            val op: String = ops.pop()
            if (isOperator(op)) {
                val val2: Double = vals.pop()
                val val1: Double = vals.pop()
                vals.push(applyOperator(op, val1, val2))
            }
            Log.d("tag11","ops: $ops")
            Log.d("tag11","vals: $vals")
            Log.d("tag11","------------")
        }

        // 値スタックに最後に入っている結果が演算結果である
        return vals.pop()
    }

    private fun applyOperator(op: String, val1: Double, val2: Double): Double {
        val b1 = BigDecimal.valueOf(val1)
        val b2 = BigDecimal.valueOf(val2)
        when (op) {
            "+" -> return b1.add(b2).toDouble()
            "-" -> return b1.subtract(b2).toDouble()
            "*" -> return b1.multiply(b2).toDouble()
            "/" -> return b1.divide(b2).toDouble()
        }
        throw RuntimeException("Unexpected operator: $op")
    }

    private fun getOpPriority(token: String): Int {
        return opPriority[token] ?: 0
    }

    private fun isOperator(token: String): Boolean {
        return opPriority.containsKey(token)
    }
}